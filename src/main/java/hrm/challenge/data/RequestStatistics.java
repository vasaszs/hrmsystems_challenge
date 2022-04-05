package hrm.challenge.data;

import java.util.HashMap;
import java.util.Iterator;

public class RequestStatistics {

	private int numberOfAddRequests;
	private int numberOfValidRequests;
	private int numberOfAddedPersons;
	private int numberOfNotValidAddRequests;
	private HashMap<String, Integer> notValidAddRequestsGroupped;

	public RequestStatistics() {
		super();
		notValidAddRequestsGroupped = new HashMap<String, Integer>();
	}

	public RequestStatistics(int numberOfAddRequests, int numberOfValidRequests, int numberOfAddedPersons,
			int numberOfNotValidAddRequests, HashMap<String, Integer> notValidAddRequestsGroupped) {

		this.numberOfAddRequests = numberOfAddRequests;
		this.numberOfValidRequests = numberOfValidRequests;
		this.numberOfAddedPersons = numberOfAddedPersons;
		this.numberOfNotValidAddRequests = numberOfNotValidAddRequests;
		this.notValidAddRequestsGroupped = notValidAddRequestsGroupped;
		notValidAddRequestsGroupped = new HashMap<String, Integer>();
	}

	public void increasenumberOfAddRequests() {
		this.numberOfAddRequests++;
	}

	public void increaseNumberOfValidRequests() {
		this.numberOfValidRequests++;
	}

	public void increaseNumberOfAddedPersons(int increment) {
		this.numberOfAddedPersons = numberOfAddedPersons + increment;
	}

	public void increaseNumberOfNotValidAddRequests() {
		this.numberOfNotValidAddRequests++;
	}

	public void increaseNumberOfAddRequests() {
		this.numberOfAddRequests++;
	}

	public HashMap<String, Integer> increaseNotValidAddRequestGrouped(String errorMessage) {

		if (this.notValidAddRequestsGroupped.containsKey(errorMessage)) {
			this.notValidAddRequestsGroupped.put(errorMessage, this.notValidAddRequestsGroupped.get(errorMessage) + 1);

		} else {
			this.notValidAddRequestsGroupped.put(errorMessage, 1);
		}

		return this.notValidAddRequestsGroupped;
	}

	@Override
	public String toString() {

		StringBuffer sbResult = new StringBuffer();

		sbResult.append("NumberOfAddRequests=");
		sbResult.append(this.getNumberOfAddRequests());
		sbResult.append("\n");

		sbResult.append("NumberOfValidRequests=");
		sbResult.append(this.getNumberOfValidRequests());
		sbResult.append("\n");

		sbResult.append("NumberOfAddedPersons=");
		sbResult.append(this.getNumberOfAddedPersons());
		sbResult.append("\n");

		sbResult.append("NumberOfNotValidAddRequests=");
		sbResult.append(this.getNumberOfNotValidAddRequests());
		sbResult.append("\n");

		sbResult.append("notValidAddRequestsGroupped->");
		if(this.notValidAddRequestsGroupped.size()==0) {
			sbResult.append(" is empty!" );
		}

		Iterator iterator = notValidAddRequestsGroupped.keySet().iterator();
		sbResult.append("\n");

		while (iterator.hasNext()) {
			String key = iterator.next().toString();
			sbResult.append(" key=");
			sbResult.append(key);
			sbResult.append(", ");
			Integer value = notValidAddRequestsGroupped.get(key);
			sbResult.append(" value=");
			sbResult.append(value);
			sbResult.append("\n");
		}

		return sbResult.toString();
	}

	public int getNumberOfAddRequests() {
		return numberOfAddRequests;
	}

	public void setNumberOfAddRequests(int numberOfAddRequests) {
		this.numberOfAddRequests = numberOfAddRequests;
	}

	public int getNumberOfValidRequests() {
		return numberOfValidRequests;
	}

	public void setNumberOfValidRequests(int numberOfValidRequests) {
		this.numberOfValidRequests = numberOfValidRequests;
	}

	public int getNumberOfAddedPersons() {
		return numberOfAddedPersons;
	}

	public void setNumberOfAddedPersons(int numberOfAddedPersons) {
		this.numberOfAddedPersons = numberOfAddedPersons;
	}

	public int getNumberOfNotValidAddRequests() {
		return numberOfNotValidAddRequests;
	}

	public void setNumberOfNotValidAddRequests(int numberOfNotValidAddRequests) {
		this.numberOfNotValidAddRequests = numberOfNotValidAddRequests;
	}

	public HashMap<String, Integer> getNotValidAddRequestsGroupped() {
		return notValidAddRequestsGroupped;
	}

	public void setNotValidAddRequestsGroupped(HashMap<String, Integer> notValidAddRequestsGroupped) {
		this.notValidAddRequestsGroupped = notValidAddRequestsGroupped;
	}


}
