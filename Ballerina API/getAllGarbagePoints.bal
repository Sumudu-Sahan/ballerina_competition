package project;

import ballerina.lang.messages;
import ballerina.lang.system;

import ballerina.net.http;
import ballerina.data.sql;

@http:configuration {basePath:"/api/getAllGarbagePoints"}
service<http> garbagepoints {
   
    
    @http:GET{}
    @http:Path {value:"/{id}"}
    resource product(message m, @http:PathParam{value:"id"} string prodId) {
        map productsMap = getGarbagePoints(prodId);
        json payload;
        payload, _ = (json) productsMap[prodId];
        message response = {};
        messages:setJsonPayload(response, payload);
        reply response;

    }
    
    
}

function getGarbagePoints(string prodId)(map productsMap) {
    map props = {"jdbcUrl":"jdbc:mysql://localhost:3306/trash_collector",
                    "username":"root", "password":""};
    sql:ClientConnector empDB = create sql:ClientConnector(props);
    
     sql:Parameter[] params = [];
    
    datatable dt = sql:ClientConnector.select(empDB,
                                    "SELECT * from garbage_post_details WHERE garbage_post_active=1", params);
    var jsonRes, _ = <json>dt;
    system:println(jsonRes);
    
     sql:ClientConnector.close(empDB);
     
     productsMap = {};
     productsMap[prodId]= jsonRes;
     
    return productsMap;
    

}