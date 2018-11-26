
export const UrlConfig = (function(){
    
    return {
                    "REGISTER_USER"         : "/munsiji-service/rest/myapp/registeruser",
                    
                    "ADD_EXPANCE"           : "/munsiji-service/rest/myapp/addexpense",
                    "CREATE_ACCOUNT"        : "/munsiji-service/rest/myapp/createaccount",
                    "LOGIN"                 : "/munsiji-service/rest/myapp/login",
                    "LOGOUT"                : "/munsiji-service/rest/myapp/logout",
                    "GET_ACCOUNT_TYPE"      : "/munsiji-service/rest/myapp/getacctypeandname?accType=",
                    "GET_ALL_EXPENCE"       : "/munsiji-service/rest/myapp/getexpense?accType="// personalexp

        };
})();

// ng build --prod --base-href=/munsiji-service/