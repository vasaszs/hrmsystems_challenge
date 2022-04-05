package hrm.challenge;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hrm.challenge.data.Person;
import hrm.challenge.data.RequestStatistics;

@Path("/persons")
public class Persons {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response persons(@Context HttpServletRequest request) {
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

		StringBuilder personHashMapToPrint = new StringBuilder();

		for (String nameActualPerson : hrmPersonHashMap.keySet()) {
			personHashMapToPrint.append(hrmPersonHashMap.get(nameActualPerson));
			personHashMapToPrint.append("\n");
		}

		Response resultResponse;

		requestStatistics.increaseNumberOfValidRequests();
		ctx.setAttribute("hrmstatistics", requestStatistics);

		resultResponse = Response.ok(personHashMapToPrint.toString(), MediaType.TEXT_PLAIN).build();

		return resultResponse;
	}

}
