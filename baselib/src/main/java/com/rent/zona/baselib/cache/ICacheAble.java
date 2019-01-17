package com.rent.zona.baselib.cache;

/**
 * Describe the behaviour of a object who can be cached
 */
public interface ICacheAble<T> {

    /**
     * set cache time, in seconds
     *
     * @param time
     * @return
     */
    ICacheAble<T> setCacheTime(long time);

    /**
     * In seconds
     *
     * @return
     */
    long getCacheTime();

    ICacheAble<T> setCacheKey(String key);

    String getCacheKey();

    /**
     * Using cache data if existent, ignore whether it is expired or not.
     *
     * @param use
     */
    ICacheAble<T> setUseCacheAnyway(boolean use);

    /**
     * check if use cache anyway
     *
     * @return
     */
    boolean useCacheAnyway();

    /**
     * @param path file path under /res, For example: "/cache_init/test.json";
     * @return
     */
    ICacheAble<T> setAssertInitDataPath(String path);

    /**
     * @return file path under /res, For example: "/cache_init/test.json";
     */
    String getAssertInitDataPath();

    /**
     * We need to process the data from data source, do some filter of convert the structure.
     * <p/>
     * As the "Assert Data" is a special data source, we also need to do the same work.
     */
    T processRawDataFromCache(String rawData);

    /**
     * when data loaded from cache
     *
     * @param cacheData
     * @param outOfDate
     */
    void onCacheData(CacheResultType cacheResultType, T cacheData, boolean outOfDate);

    /**
     * When there is no cache data is available, this method will be called.
     * Will always in Main UI thread
     */
    void onNoCacheData(CacheManager cacheManager);

    /**
     * temporarily disable cache. The data will no be load from cache and will also not be put into cache
     */
    ICacheAble<T> setDisableCache(boolean disable);

    /**
     * check if cache is disabled
     *
     * @return
     */
    boolean cacheIsDisabled();
}
