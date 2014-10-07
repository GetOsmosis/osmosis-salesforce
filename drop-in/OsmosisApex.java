public class OsmosisIntegration {
   /* public class ExternalOpportunity {
        public String id {get; set;}
        public Integer proposal_id {get; set;}
    }*/

    @future (callout=true)
    public static void postOpportunity(Id opportunity) {
        JSONGenerator gen = JSON.createGenerator(true);
        gen.writeStartArray();

        gen.writeStartObject();

        Opportunity op =
          [select Id, AccountId, Name
           from opportunity
           where id = '006o0000004cehiAAA'][0];

				gen.writeStringField('Opportunity_Id', op.Id);
        gen.writeStringField('Opportunity_AccountId', op.AccountId);
        gen.writeStringField('Opportunity_Name', op.Name);

        List<Contact> contacts = [
          select Id, Email, Fax, FirstName, LastName, Name
          from Contact
          where accountid = '001o000000ASzxKAAT'];

				gen.writeFieldName('contacts');
        gen.writeStartArray();
				for (Contact c : contacts) {
  				gen.writeStartObject();

					gen.writeStringField('Contact_Id', c.Id);
					gen.writeStringField('Contact_Email', c.Email);
					gen.writeStringField('Contact_Fax', c.Fax);
					gen.writeStringField('Contact_FirstName', c.FirstName);
					gen.writeStringField('Contact_LastName', c.LastName);
					gen.writeStringField('Contact_Name', c.Name);

	  			gen.writeEndObject();		  	
				}
	      gen.writeEndArray();

        gen.writeEndObject();
        gen.writeEndArray();

        String jsonOpportunity = gen.getAsString();
        System.debug('jsonOpportunity: ' + jsonOpportunity);

        HttpRequest req = new HttpRequest();

        req.setMethod('POST');

        req.setEndpoint('https://nameless-woodlawn.herokuapp.com/order');
        req.setHeader('Content-Type', 'application/json');
        req.setBody(jsonOpportunity);

        Http http = new Http();

        HTTPResponse res = http.send(req);

        System.debug('Fulfillment service returned '+ res.getBody());

        if (res.getStatusCode() != 200) {
            System.debug('Error from ' + req.getEndpoint() + ' : ' +
                    res.getStatusCode() + ' ' + res.getStatus());
        }
        else {
					// TODO update the opportunity with a proposal ID
 /*           List<Invoice__c> invoices =
                [SELECT Id FROM Invoice__c WHERE Id IN :invoiceIds];
            List<ExternalOpportunity> orders =
                    (List<ExternalOpportunity>)JSON.deserialize(res.getBody(),
                            List<ExternalOpportunity>.class);

            Map<Id, Invoice__c> invoiceMap =
                    new Map<Id, Invoice__c>(invoices);

            for ( ExternalOpportunity order : orders ) {
                Invoice__c invoice = invoiceMap.get(order.id);
                invoice.OrderId__c = String.valueOf(order.order_number);
            }

            update invoices;*/
        }
    }
}
