
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
                    "RESET_PASSWORD"        : "/munsiji-service/rest/myapp/resetpassword",
                    "UPDATE_ACC_SCOPE"      : "/munsiji-service/rest/myapp/changeaccountstate",

                    "SSE_CALL"              : "/munsiji-service/rest/myapp/streamssemvc?reqKey=",
                    "KEEP_ALIVE"            : "/munsiji-service/rest/myapp/keepssesessionalive?reqKey="

        };
})();

// ng build --prod --base-href=/munsiji-service/

/*
http://localhost:8080/munsiji-service/rest/myapp/streamssemvc?reqKey=<cacheSessionId>
Response will be  String only for now testing purpose after 30 seconds.. later we will change this timeout value as our need.

-> need to call below URL to let server know, client-connection is alive and not closed.  else automatically above connection will get closed after 30 seconds 
http://localhost:8080/munsiji-service/rest/myapp/keepssesessionalive?reqKey=<cacheSessionId>   */