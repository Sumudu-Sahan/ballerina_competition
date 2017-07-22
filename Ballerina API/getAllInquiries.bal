package project;

import ballerina.lang.messages;
import ballerina.lang.system;

import ballerina.net.http;
import ballerina.data.sql;

@http:configuration {basePath:"/api/getAllInquiries"}
service<http> productmgt {
   
    
    @http:GET{}
    @http:Path {value:"/{id}"}
    resource product(message m, @http:PathParam{value:"id"} string prodId) {
        map productsMap = populateSampleProducts(prodId);
        json payload;
        payload, _ = (json) productsMap[prodId];
        message response = {};
        messages:setJsonPayload(response, payload);
        reply response;

    }
    
    
}

function populateSampleProducts(string prodId)(map productsMap) {
    map props = {"jdbcUrl":"jdbc:mysql://localhost:3306/trash_collector",
                    "username":"root", "password":""};
    sql:ClientConnector empDB = create sql:ClientConnector(props);
    
     sql:Parameter[] params = [];
    
    datatable dt = sql:ClientConnector.select(empDB,
                                    "SELECT * from garbage_post_details JOIN user_details ON "+ "("+"garbage_post_details.garbage_post_user_id = user_details.user_id"
	
+")"+" WHERE garbage_post_active = 1", params);
    var jsonRes, _ = <json>dt;
    system:println(jsonRes);
    
     sql:ClientConnector.close(empDB);
     
     productsMap = {};
     productsMap[prodId]= jsonRes;
     
    return productsMap;
    

}