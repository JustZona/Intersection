package com.rent.zona.commponent.ui.bean;

import com.yanzhenjie.permission.Permission;

import java.util.ArrayList;
import java.util.List;

public class LoginUser {

    /**
     * userInfo :
     * roleAuthList :
     * token : {"userId":"N3Ms5ERTgu7=","token":"37zVtg8YWnSnCiJscqK+Jw=="}
     */

    private UserInfo userInfo;
    private ArrayList<RoleAuthList> roleAuthList;
    private TokenBean token;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<RoleAuthList> getRoleAuthList() {
        return roleAuthList;
    }

    public void setRoleAuthList(ArrayList<RoleAuthList> roleAuthList) {
        this.roleAuthList = roleAuthList;
    }

    public TokenBean getToken() {
        return token;
    }

    public void setToken(TokenBean token) {
        this.token = token;
    }

    public static class TokenBean {
        /**
         * userId : N3Ms5ERTgu7=
         * token : 37zVtg8YWnSnCiJscqK+Jw==
         */

        private String userId;
        private String token;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
    class UserInfo{

        /**
         * realName : 111
         * phone : 55555
         * userId : 36
         */

        private String realName;
        private String phone;
        private int userId;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
    static class RoleAuthList{

        /**
         * id : 33
         * roleName : 财务员
         * isAuthEdit : 1
         * menulist : [{"id":"24","menuName":"测试管理2","parentId":"0","menuUrl":"/test/1","icon":"http://qft-oss.oss-cn-qingdao.aliyuncs.com/zhengzunew1.png","childs":[],"menuAuthList":null},{"id":"23","menuName":"测试管理1","parentId":"0","menuUrl":"/test/0","icon":"http://qft-oss.oss-cn-qingdao.aliyuncs.com/zhengzunew1.png","childs":[],"menuAuthList":null}]
         */

        private String id;
        private String roleName;
        private int isAuthEdit;
        private List<MenulistBean> menulist;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public int getIsAuthEdit() {
            return isAuthEdit;
        }

        public void setIsAuthEdit(int isAuthEdit) {
            this.isAuthEdit = isAuthEdit;
        }

        public List<MenulistBean> getMenulist() {
            return menulist;
        }

        public void setMenulist(List<MenulistBean> menulist) {
            this.menulist = menulist;
        }

        public static class MenulistBean {
            /**
             * id : 24
             * menuName : 测试管理2
             * parentId : 0
             * menuUrl : /test/1
             * icon : http://qft-oss.oss-cn-qingdao.aliyuncs.com/zhengzunew1.png
             * childs : []
             * menuAuthList : null
             */

            private String id;
            private String menuName;
            private String parentId;
            private String menuUrl;
            private String icon;
            private Object menuAuthList;
            private List<?> childs;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMenuName() {
                return menuName;
            }

            public void setMenuName(String menuName) {
                this.menuName = menuName;
            }

            public String getParentId() {
                return parentId;
            }

            public void setParentId(String parentId) {
                this.parentId = parentId;
            }

            public String getMenuUrl() {
                return menuUrl;
            }

            public void setMenuUrl(String menuUrl) {
                this.menuUrl = menuUrl;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public Object getMenuAuthList() {
                return menuAuthList;
            }

            public void setMenuAuthList(Object menuAuthList) {
                this.menuAuthList = menuAuthList;
            }

            public List<?> getChilds() {
                return childs;
            }

            public void setChilds(List<?> childs) {
                this.childs = childs;
            }
        }
    }
}
