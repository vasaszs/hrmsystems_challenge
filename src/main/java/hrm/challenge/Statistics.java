package hrm.challenge;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import hrm.challenge.data.RequestStatistics;

@Path("/statistics")
public class Statistics {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response statistics(@Context HttpServletRequest request) {
		ServletContext ctx = request.getServletContext();
		RequestStatistics requestStatistics = (RequestStatistics) ctx.getAttribute("hrmstatistics");
		if (requestStatistics == null) {
			requestStatistics = new RequestStatistics();
		}

		Response resultResponse;
		String responseText;

		requestStatistics.increaseNumberOfValidRequests();
		responseText = requestStatistics.toString();

		ctx.setAttribute("hrmstatistics", requestStatistics);

		resultResponse = Response.ok(responseText, MediaType.TEXT_PLAIN).build();

		return resultResponse;
	}

}
