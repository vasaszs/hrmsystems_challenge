package hrm.challenge;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import hrm.challenge.data.Person;
import hrm.challenge.data.RequestStatistics;
import hrm.challenge.util.PersonExtractor;

@Path("/persons/change")
public class PersonsChange {

	@POST
	public Response persons(String rawXMLinBody, @Context HttpServletRequest request) {
		ServletContext ctx = request.getServletContext();
		RequestStatistics requestStatistics = (RequestStatistics) ctx.getAttribute("hrmstatistics");
		if (requestStatistics == null) {
			requestStatistics = new RequestStatistics();
		}
		ConcurrentHashMap<String, Person> hrmPersonHashMap = (ConcurrentHashMap<String, Person>) ctx
				.getAttribute("hrmpersonhashmap");
		if (hrmPersonHashMap == null) {
			hrmPersonHashMap = new ConcurrentHashMap<String, Person>();
		}

		ConcurrentHashMap<String, Person> personToBeChangedHashMap = null;

		Status httpResponseCode = Response.Status.OK;
		Response resultResponse;

		try {
			personToBeChangedHashMap = PersonExtractor.extractPersons(rawXMLinBody);
		} catch (SAXException e) {

			httpResponseCode = Response.Status.BAD_REQUEST;

		} catch (IOException e) {

			httpResponseCode = Response.Status.BAD_REQUEST;

		} catch (ParserConfigurationException e) {

			httpResponseCode = Response.Status.BAD_REQUEST;

		}

		if (httpResponseCode.compareTo(Response.Status.OK) == 0) {
			resultResponse = Response.ok("Person(s) changed successfully.", MediaType.TEXT_PLAIN).build();
			if (personToBeChangedHashMap.size() == 0) {
				resultResponse = Response.ok("There is no Person to be changed!", MediaType.TEXT_PLAIN).build();
			}

			for (String nameActualPerson : personToBeChangedHashMap.keySet()) {
				if (hrmPersonHashMap.containsKey(nameActualPerson)) {
					hrmPersonHashMap.put(nameActualPerson, personToBeChangedHashMap.get(nameActualPerson));
					resultResponse = Response.ok("Person(s) changed successfully.", MediaType.TEXT_PLAIN).build();
				} else {
					resultResponse = Response.ok("There is no person with this name!", MediaType.TEXT_PLAIN).build();
				}
			}

			ctx.setAttribute("hrmpersonhashmap", hrmPersonHashMap);
			requestStatistics.increaseNumberOfValidRequests();

		} else {
			resultResponse = Response.status(httpResponseCode).entity("Data not acceptable").build();
		}

		ctx.setAttribute("hrmstatistics", requestStatistics);

		return resultResponse;
	}

}
