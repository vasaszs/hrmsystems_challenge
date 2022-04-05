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

@Path("/persons/add")
public class PersonsAdd {

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

		ConcurrentHashMap<String, Person> personToBeAddedHashMap = null;
		Status httpResponseCode = Response.Status.OK;

		try {
			personToBeAddedHashMap = PersonExtractor.extractPersons(rawXMLinBody);
		} catch (SAXException e) {

			requestStatistics.increaseNotValidAddRequestGrouped(Response.Status.BAD_REQUEST.toString());
			httpResponseCode = Response.Status.BAD_REQUEST;

		} catch (IOException e) {
			requestStatistics.increaseNotValidAddRequestGrouped(Response.Status.BAD_REQUEST.toString());
			httpResponseCode = Response.Status.BAD_REQUEST;

		} catch (ParserConfigurationException e) {
			requestStatistics.increaseNotValidAddRequestGrouped(Response.Status.BAD_REQUEST.toString());
			httpResponseCode = Response.Status.BAD_REQUEST;
		}
		Response resultResponse;

		int actuallyAddedPersonNum = 0;
		if (httpResponseCode.compareTo(Response.Status.OK) == 0) {
			for (String nameActualPerson : personToBeAddedHashMap.keySet()) {

				if (!hrmPersonHashMap.containsKey(nameActualPerson)) {
					hrmPersonHashMap.put(nameActualPerson, personToBeAddedHashMap.get(nameActualPerson));
					actuallyAddedPersonNum++;
				}

			}

			ctx.setAttribute("hrmpersonhashmap", hrmPersonHashMap);
			requestStatistics.increaseNumberOfAddedPersons(actuallyAddedPersonNum);
			requestStatistics.increasenumberOfAddRequests();
			requestStatistics.increaseNumberOfValidRequests();

			resultResponse = Response.ok(actuallyAddedPersonNum + " Person(s) added successfully, so the new sum is "
					+ hrmPersonHashMap.size(), MediaType.TEXT_PLAIN).build();
		} else {

			resultResponse = Response.status(httpResponseCode).entity("Data not acceptable").build();
			requestStatistics.increaseNumberOfNotValidAddRequests();
		}

		ctx.setAttribute("hrmstatistics", requestStatistics);

		return resultResponse;

	}

}
