package feedback;

import java.sql.SQLException;
import java.util.ArrayList;

public class testMarkObject {
	
	public  Criteria createCriteria() throws SQLException {
		Criteria c = new Criteria();
		SubSection ss = new SubSection();
		SubSection ss2 = new SubSection();
		ShortText st = new ShortText();
		ShortText st2 = new ShortText();
		ArrayList<String> longtext1 = new ArrayList<String>();
		longtext1.add("The University of Melbourne is a public "
				+ "research university located in Melbourne, Australia. "
				+ "Founded in 1853, it is Australia's second oldest "
				+ "university and the oldest in "
				+ "Victoria. Melbourne's main campus is located in "
				+ "Parkville, an inner suburb north of the Melbourne central "
				+ "business district, with several other campuses located across Victoria.");
		st.setLongtext(longtext1);
		st.setName("short text name");
		ArrayList<String> longtext2 = new ArrayList<String>();
		longtext2.add("The Parkville Campus is the primary campus of "
				+ "the university.Originally established in a large area "
				+ "north of Grattan Street in Parkville, the campus "
				+ "has expanded well beyond its boundaries, with many "
				+ "of its newly acquired buildings located "
				+ "in the nearby suburb of Carlton. ");
		st2.setLongtext(longtext2);
		ArrayList<ShortText> shortTextList = new ArrayList<ShortText>();
		shortTextList.add(st);
		ss.setShortTextList(shortTextList);
		ss.setName("Introduction General");
		ArrayList<ShortText> shortTextList2 = new ArrayList<ShortText>();
		shortTextList2.add(st2);
		ss2.setShortTextList(shortTextList2);
		ss2.setName("Presentation Length");
		ArrayList<SubSection> subsectionList = new ArrayList<SubSection>();
		subsectionList.add(ss);
		subsectionList.add(ss2);
		c.setSubsectionList(subsectionList);
		c.setMarkIncrement("1/2");
		c.setMaximunMark(10);
		c.setName("Test Criteria");
		c.setWeighting(25);
		System.out.println("have create criteria!");
		return c;
	}
	
	public ArrayList<Mark> markList() throws SQLException{
		
		ArrayList<Mark> markList = new ArrayList<Mark>();
		Mark mark1 = new Mark();
		Criteria c = createCriteria();
		Criteria c1 = createCriteria();
		Criteria c2 = createCriteria();
		Criteria c3 = createCriteria();
		Criteria c4 = createCriteria();
		c.setName("Presentation Structure");
		c1.setName("Test Criteria 2");
		c2.setName("Test Criteria 3");
		c3.setName("Test Criteria 4");
		c4.setName("Only Comment Test");


		mark1.getCriteriaList().add(c);
		mark1.getCriteriaList().add(c1);
		mark1.getCriteriaList().add(c2);
		mark1.getCriteriaList().add(c3);
		mark1.getCommentList().add(c4);
		mark1.setComment("");
		mark1.getMarkList().add(7.5);
		mark1.getMarkList().add(7.5);
		mark1.getMarkList().add(6.0);
		mark1.getMarkList().add(6.5);
		mark1.setTotalMark(88.99);
		mark1.setLecturerName("Trump");
		
		markList.add(mark1);
		
		mark1.setTotalMark(77.66);
		mark1.setLecturerName("Mergen");
		
		markList.add(mark1);
		
		return markList;
		
	}
}
