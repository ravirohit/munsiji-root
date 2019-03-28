
export const UrlConfig = (function(){
    
    return {
                    "REGISTER_USER"         : "/munsiji-service/rest/myapp/registeruser",
                    
                    "ADD_EXPANCE"           : "/munsiji-service/rest/myapp/addexpense",
                    "CREATE_ACCOUNT"        : "/munsiji-service/rest/myapp/createaccount",
                    "LOGIN"                 : "/munsiji-service/rest/myapp/login",
                    "LOGOUT"                : "/munsiji-service/rest/myapp/logout",
                    "FORGETPWD"             : "/munsiji-service/rest/myapp/forgetpassword?emailId=",
                    "GET_ACCOUNT_TYPE"      : "/munsiji-service/rest/myapp/getacctypeandname?accType=",
                    "GET_ALL_EXPENCE"       : "/munsiji-service/rest/myapp/getexpense?accType=",// personalexp
                    "GET_PROFILE_INFO"      : "/munsiji-service/rest/myapp/getprofile",
                    "RESET_PASSWORD"        : "/munsiji-service/rest/myapp/resetpassword"

        };
})();

// ng build --prod --base-href=/munsiji-service/