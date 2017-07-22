package project;

import ballerina.lang.messages;
import ballerina.lang.system;

import ballerina.net.http;
import ballerina.data.sql;

@http:configuration {basePath:"/api/getDriver"}
service<http> getdriver {
   
    
    @http:GET{}
    @http:Path {value:"/{id}"}
    resource product(message m, @http:PathParam{value:"id"} string driverId) {
        map productsMap = getDriver(driverId);
        json payload;
        payload, _ = (json) productsMap[driverId];
        message response = {};
        messages:setJsonPayload(response, payload);
        reply response;

    }
    
    
}

function getDriver(string driverId)(map productsMap) {
    map props = {"jdbcUrl":"jdbc:mysql://localhost:3306/trash_collector",
                    "username":"root", "password":""};
    sql:ClientConnector empDB = create sql:ClientConnector(props);
    
     sql:Parameter[] params = [];
    
    datatable dt = sql:ClientConnector.select(empDB,
                                    "SELECT * FROM user_details WHERE user_role=3 AND user_id ="+driverId, params);
    var jsonRes, _ = <json>dt;
    system:println(jsonRes);
    
     sql:ClientConnector.close(empDB);
     
     productsMap = {};
     productsMap[driverId]= jsonRes;
     
    return productsMap;
    

}