package com.rent.zona.baselib.rx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;


public final class SafeCompositeDisposable implements Disposable {

    private Set<Disposable> disposables;
    private volatile boolean isDisposed;

    public SafeCompositeDisposable() {
    }

    public SafeCompositeDisposable(final Disposable... subscriptions) {
        this.disposables = new HashSet<Disposable>(Arrays.asList(subscriptions));
    }


    @Override
    public boolean isDisposed() {
        return isDisposed;
    }

    /**
     * Adds a new {@link Disposable} to this {@code CompositeSubscription} if the
     * {@code CompositeSubscription} is not yet unsubscribed. If the {@code CompositeSubscription} <em>is</em>
     * unsubscribed, {@code add} will indicate this by explicitly unsubscribing the new {@code Subscription} as
     * well.
     *
     * @param d the {@link Disposable} to add
     */
    public void add(final Disposable d) {
        if (d.isDisposed()) {
            return;
        }

        clearUnsubscribes();

        if (!isDisposed) {
            synchronized (this) {
                if (!isDisposed) {
                    if (disposables == null) {
                        disposables = new HashSet<Disposable>(4);
                    }
                    disposables.add(d);
                    return;
                }
            }
        }
        // call after leaving the synchronized block so we're not holding a lock while executing this
        d.dispose();
    }

    /**
     * Removes a {@link Disposable} from this {@code CompositeSubscription}, and unsubscribes the
     * {@link Disposable}.
     *
     * @param d the {@link Disposable} to remove
     */
    public void remove(final Disposable d) {
        if (!isDisposed) {
            boolean unsubscribe = false;
            synchronized (this) {
                if (isDisposed || disposables == null) {
                    return;
                }
                unsubscribe = disposables.remove(d);
            }
            if (unsubscribe) {
                // if we removed successfully we then need to call unsubscribe on it (outside of the lock)
                d.dispose();
            }
        }
    }

    /**
     * Unsubscribes any subscriptions that are currently part of this {@code CompositeSubscription} and remove
     * them from the {@code CompositeSubscription} so that the {@code CompositeSubscription} is empty and
     * able to manage new subscriptions.
     */
    public void clear() {
        if (!isDisposed) {
            Collection<Disposable> unsubscribe = null;
            synchronized (this) {
                if (isDisposed || disposables == null) {
                    return;
                } else {
                    unsubscribe = disposables;
                    disposables = null;
                }
            }
            unsubscribeFromAll(unsubscribe);
        }
    }

    public void clearUnsubscribes() {
        if (!isDisposed) {
            synchronized (this) {
                if (isDisposed || disposables == null || disposables.size() == 0) {
                    return;
                } else {
                    Collection<Disposable> unsubscribe = new HashSet<Disposable>(disposables.size());
                    for (Disposable s : unsubscribe) {
                        if (s.isDisposed()) {
                            unsubscribe.add(s);
                        }
                    }
                    disposables.removeAll(unsubscribe);
                }
            }
        }
    }

    /**
     * Unsubscribes itself and all inner subscriptions.
     * <p>After call of this method, new {@code Subscription}s added to {@link SafeCompositeDisposable}
     * will be unsubscribed immediately.
     */
    @Override
    public void dispose() {
        if (!isDisposed) {
            Collection<Disposable> unsubscribe = null;
            synchronized (this) {
                if (isDisposed) {
                    return;
                }
                isDisposed = true;
                unsubscribe = disposables;
                disposables = null;
            }
            // we will only get here once
            unsubscribeFromAll(unsubscribe);
        }
    }

    private static void unsubscribeFromAll(Collection<Disposable> disposables) {
        if (disposables == null) {
            return;
        }
        List<Throwable> es = null;
        for (Disposable s : disposables) {
            try {
                s.dispose();
            } catch (Throwable e) {
                if (es == null) {
                    es = new ArrayList<Throwable>();
                }
                es.add(e);
            }
        }
//        Exceptions.throwIfAny(es);
        if (es != null && !es.isEmpty()) {
            if (es.size() == 1) {
                Throwable t = es.get(0);
                // had to manually inline propagate because some tests attempt StackOverflowError
                // and can't handle it with the stack space remaining
                if (t instanceof RuntimeException) {
                    throw (RuntimeException) t;
                } else if (t instanceof Error) {
                    throw (Error) t;
                } else {
                    throw new RuntimeException(t); // NOPMD
                }
            }
            throw new CompositeException(es);
        }
    }

    /**
     * Returns true if this composite is not unsubscribed and contains subscriptions.
     *
     * @return {@code true} if this composite is not unsubscribed and contains subscriptions.
     * @since 1.0.7
     */
    public boolean hasSubscriptions() {
        if (!isDisposed) {
            synchronized (this) {
                return !isDisposed && disposables != null && !disposables.isEmpty();
            }
        }
        return false;
    }
}
