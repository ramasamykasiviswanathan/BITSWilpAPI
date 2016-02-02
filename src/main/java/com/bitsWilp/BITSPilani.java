package com.bitsWilp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * @author Ramasamy Kasiviswanathan
 *
 */
public class BITSPilani {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			System.out.println("Enter Course ID:");
			final LinkedList<WILPObject> wilpObjects = new LinkedList<>();
			int index = 0, iterate = 0;
			Response response = Jsoup
					.connect(
							"https://wilp-bits-pilani.webex.com/cmp3000/webcomponents/calendar/calendar.do?siteurl=wilp-bits-pilani&serviceType=TC&ownerID=0&pageNum=1&timezoneID=0&year=2016&month=0&date=15&showpast=false&showreg=false&tabType=search&searchKeyWord="
									+ reader.readLine())
					.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:43.0) Gecko/20100101 Firefox/43.0")
					.timeout(120 * 1000).execute();
			if (response.statusCode() == 200) {
				Document document = response.parse();
				document.select("td.TblNormalBgColor > font.TblSixthFont").forEach(element -> {
					WILPObject wilpObject = new WILPObject();
					wilpObject.setDate(element.ownText());
					wilpObjects.add(wilpObject);
				});
				for (Element node : document.select("td [nowrap=\"\"] > font[class$=Font3]:only-child")) {
					if (node.childNodeSize() == 1) {
						switch (index) {
						case 0: {
							wilpObjects.get(iterate).setStartTime(node.ownText());
							++index;
							break;
						}
						case 1: {
							wilpObjects.get(iterate).setCourseID(node.ownText());
							++index;
							break;
						}
						default: {
							wilpObjects.get(iterate).setDuration(node.ownText());
							index = 0;
							++iterate;
						}
						}
					}
				}
				if (wilpObjects.size() >= 1)
					System.out.println(wilpObjects);
				else
					System.err.println("Currently, there are no sessions scheduled.");
			}
		}
	}

}
