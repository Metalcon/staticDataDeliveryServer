package de.metalcon.socialgraph.algorithms;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import de.metalcon.socialgraph.SocialGraph;

public class AlgorithmTest {

	/**
	 * single user indices
	 */
	protected final int USER_INDEX_A = 0, USER_INDEX_B = 1, USER_INDEX_C = 2,
			USER_INDEX_D = 3, USER_INDEX_E = 4;

	/**
	 * single users
	 */
	protected final Node USER_A = AlgorithmTests.USERS[this.USER_INDEX_A],
			USER_B = AlgorithmTests.USERS[this.USER_INDEX_B],
			USER_C = AlgorithmTests.USERS[this.USER_INDEX_C],
			USER_D = AlgorithmTests.USERS[this.USER_INDEX_D],
			USER_E = AlgorithmTests.USERS[this.USER_INDEX_E];

	/**
	 * social graph algorithm being tested
	 */
	protected SocialGraph algorithm;

	/**
	 * test transaction
	 */
	protected Transaction transaction;

	/**
	 * wait the number of milliseconds specified
	 * 
	 * @param ms
	 *            delay in milliseconds
	 */
	protected static void waitMs(final long ms) {
		final long targetMs = System.currentTimeMillis() + ms;
		while (System.currentTimeMillis() < targetMs) {
			// wait
		}
	}

	/**
	 * count the number of relationships of an iterable
	 * 
	 * @param relationships
	 *            iterable relationships
	 * @return number of relationships available
	 */
	protected static int getNumberOfRelationships(
			final Iterable<Relationship> relationships) {
		final Iterator<Relationship> iter = relationships.iterator();
		int result = 0;
		while (iter.hasNext()) {
			result += 1;
			iter.next();
		}
		return result;
	}

	/**
	 * extract the messages of the status updates
	 * 
	 * @param activities
	 *            list of status updates in Activity JSON format
	 * @return list containing the status update messages
	 */
	protected static List<Long> extractStatusUpdateMessages(
			final List<String> activities) {
		final List<Long> statusUpdateMessages = new LinkedList<Long>();

		int index;
		String message;
		for (String activity : activities) {
			index = activity.indexOf("message");
			message = activity.substring(index + 10,
					activity.indexOf("}", index) - 1);
			statusUpdateMessages.add(Long.valueOf(message));
		}

		return statusUpdateMessages;
	}

	/**
	 * compare the expected messages with the ones in the activities
	 * 
	 * @param messages
	 *            status update messages expected
	 * @param activities
	 *            list of status updates in Activity JSON format
	 */
	protected static void compareValues(final long[] messages,
			final List<String> activities) {
		final List<Long> statusUpdateNodeMessages = extractStatusUpdateMessages(activities);
		assertEquals(statusUpdateNodeMessages.size(), messages.length);

		int i = 0;
		for (long message : statusUpdateNodeMessages) {
			assertEquals(message, messages[i]);
			i += 1;
		}
	}

}
