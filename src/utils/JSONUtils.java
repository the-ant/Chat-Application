package utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import pojo.CurrentUser;
import pojo.Group;
import pojo.Message;
import pojo.StructureDB;
import pojo.User;

public class JSONUtils {

	public static JSONObject parseJSON(String json) {
		JSONObject obj = new JSONObject(json);
		return obj;
	}

	public static List<Message> parseMessages(JSONArray array) {
		List<Message> result = new ArrayList<Message>();
		if (array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject msgObj = array.getJSONObject(i);
				Message msg = parseMessageFromJSONObject(msgObj);
				if (msg != null) {
					result.add(msg);
				}
			}
		}
		return result;
	}

	private static Message parseMessageFromJSONObject(JSONObject msgObj) {
		Message result = null;
		if (msgObj != null) {
			int groupID = msgObj.getInt(StructureDB.MESSAGE_GROUP_ID);
			String message = msgObj.getString(StructureDB.MESSAGE);
			int userID = msgObj.getInt(StructureDB.MESSAGE_USER_ID);
			boolean isFile = msgObj.getBoolean(StructureDB.MESSAGE_IS_FILE);

			result = new Message(groupID, userID, message, isFile);
			if (userID == CurrentUser.getInstance().getUser_id()) {
				result.setMe(true);
			}
		}
		return result;
	}

	public static List<User> parseFriends(JSONArray array) {
		List<User> result = new ArrayList<User>();
		if (array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject userObj = array.getJSONObject(i);
				User user = parseUserFromJSONObject(userObj);
				if (user != null) {
					result.add(user);
				}
			}
		}
		return result;
	}

	public static User parseUserFromJSONObject(JSONObject userObj) {
		User result = null;
		if (userObj != null) {
			int id = userObj.getInt(StructureDB.USER_ID);
			String fullname = userObj.getString(StructureDB.USER_FULLNAME);
			boolean online = userObj.getBoolean(StructureDB.ONLINE);
			result = new User(id, fullname, online);
		}
		return result;
	}

	public static List<Group> parseGroups(JSONArray array) {
		List<Group> result = new ArrayList<Group>();
		if (array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject userObj = array.getJSONObject(i);
				Group group = parseGroupFromJSONObject(userObj);
				if (group != null) {
					result.add(group);
				}
			}
		}
		return result;
	}

	public static Group parseGroupFromJSONObject(JSONObject groupObj) {
		Group result = null;
		if (groupObj != null) {
			int groupId = groupObj.getInt(StructureDB.GROUP_ID);
			String name = groupObj.getString(StructureDB.GROUP_NAME);
			int userIdCreated = groupObj.getInt(StructureDB.GROUP_USER_ID_CREATED);
			boolean isChatGroup = groupObj.getBoolean(StructureDB.GROUP_IS_CHAT_GROUP);

			List<Integer> listUserIdInGroup = new ArrayList<>();
			String usersStr = groupObj.getString(StructureDB.GROUP_LIST_USERS);
			for (String e : usersStr.split("[,]")) {
				listUserIdInGroup.add(Integer.parseInt(e));
			}
			result = new Group(groupId, name, userIdCreated, isChatGroup, listUserIdInGroup);
		}
		return result;
	}

	public static List<User> parseAllUser(JSONArray jsonArray) {
		List<User> result = new ArrayList<>();
		if (jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject userObj = jsonArray.getJSONObject(i);
				User user = parseUserFromJSONObject(userObj);
				if (user != null) {
					result.add(user);
				}
			}
		}
		return result;
	}

	public static List<User> parseAllUserRequests(JSONArray array) {
		List<User> result = new ArrayList<>();
		if (array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject requestObj = array.getJSONObject(i);
				User user = parseUserRequestFromJSONObject(requestObj);
				if (user != null) {
					result.add(user);
				}
			}
		}
		return result;
	}

	public static User parseUserRequestFromJSONObject(JSONObject requestObj) {
		User result = null;
		if (requestObj != null) {
			int userId = requestObj.getInt(StructureDB.USER_ID);
			String fullname = requestObj.getString(StructureDB.USER_FULLNAME);
			result = new User(userId, fullname);
		}
		return result;
	}
}
