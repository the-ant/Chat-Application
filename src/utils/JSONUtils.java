package utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import pojo.Group;
import pojo.StructureDB;
import pojo.User;

public class JSONUtils {

	public static JSONObject parseJSON(String json) {
		JSONObject obj = new JSONObject(json);
		return obj;
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
	
	public static List<Group> parseGroups(JSONArray array){
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
			for (String  e : usersStr.split("[,]")) {
				listUserIdInGroup.add(Integer.parseInt(e));
			}
			result = new Group(groupId, name, userIdCreated, isChatGroup, listUserIdInGroup);
		}
		return result;
	}
}
