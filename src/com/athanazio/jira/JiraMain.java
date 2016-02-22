package com.athanazio.jira;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class JiraMain {
	// POST /rest/auth/1/session


	public static void main(String[] args) throws Exception {

		Map properties = System.getenv();
		String username = (String) properties.get("JIRA_USERNAME");
		String password = (String) properties.get("JIRA_PASSWORD");
		String domain = (String) properties.get("JIRA_DOMAIN");

		JiraApiCall api = new JiraApiCall(domain, username, password);
		System.out.println(api);

		// String foo = api.connect();

		JiraProject[] projects = api.getAllProjects();
		System.out.println("projects=" + Arrays.toString(projects));

		File target = new File("download");
		if(!target.exists()){
			target.mkdir();
		}
		
		for (int i = 0; i < projects.length; i++) {
			System.out.println("looking for project folder : " + projects[i].key + "=" + projects[i].name);
			File projectFolder = new File("download/" + projects[i].key );
			if( ! projectFolder.exists()){
				projectFolder.mkdir();
			}
			
			saveProjectIcon(api, projects[i], projectFolder);
			JiraIssue[] issues = api.getIssues(projects[i].key);
		}

		api.cleanup();

		// List<ZenTicket> results = api.findNotSolvedTicketsByTag("frm");
		// System.out.println(results.size() + " tickets encontrados");
		// System.out.println("-----");
		//
		// Iterator<ZenTicket> iterator = results.iterator();
		// while (iterator.hasNext()) {
		// ZenTicket ticket = iterator.next();
		// System.out.println(ticket);
		// }

	}

	private static JiraIssue[] getIssues(JiraApiCall api, JiraProject jiraProject) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void saveProjectIcon(JiraApiCall api, JiraProject jiraProject, File projectFolder) throws Exception {
		
		File targetFile = new File( projectFolder.getPath() + "/icon.svg");
		api.saveFile( jiraProject.avatarUrls.get("48x48"), targetFile );

		
	}

}
