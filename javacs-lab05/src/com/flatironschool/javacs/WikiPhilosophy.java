package com.flatironschool.javacs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import org.jsoup.select.Elements;

public class WikiPhilosophy {
	
	final static WikiFetcher wf = new WikiFetcher();
	
	/**
	 * Tests a conjecture about Wikipedia and Philosophy.
	 * 
	 * https://en.wikipedia.org/wiki/Wikipedia:Getting_to_Philosophy
	 * 
	 * 1. Clicking on the first non-parenthesized, non-italicized link
     * 2. Ignoring external links, links to the current page, or red links
     * 3. Stopping when reaching "Philosophy", a page with no links or a page
     *    that does not exist, or when a loop occurs
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		
        // some example code to get you started
		if (args.length > 0)
        {
			String url = args[0];
			
			Map<Integer, String> map = new HashMap<Integer, String>();
			int i = 0;
			int leftParens = 0;
			
            map.put(i, url);
            i++;
            
            boolean done = false;
            boolean success = false;
            while(!done)
            {
            	Elements paragraphs = wf.fetchWikipedia(url);
            	Element firstPara = paragraphs.get(0);
            	int k = 1;
            	
            	boolean done2 = false;
            	
            	while(!done2)
            	{
	            	Iterable<Node> iter = new WikiNodeIterable(firstPara);
	        		for (Node node: iter) 
	        		{
	        			if (node instanceof TextNode)
	        			{
	        				String str = node.toString();
	        				if(str.indexOf("(") != -1)
	        				{
	        					leftParens++;
	        				}
	        				if(str.indexOf(")") != -1)
	        				{
	        					if(leftParens > 0)
	        					{
	        						leftParens--;
	        					}
	        				}
	        			}
	        			else
	        			{
	        				if(leftParens == 0)
	        				{
		        				Element e = (Element)node;
		                        String tName = e.tagName();
		                        if(tName.equals("A")||tName.equals("a"))
		                        {
		                        	Element parnt = e.parent();
		                        	String tNameParnt = parnt.tagName();
		                        	if(!(tName.equals("I")||tName.equals("i")
		                        			||tName.equals("EM")||tName.equals("em")))
		                        	{
			                        	String absHref = e.attr("abs:href");
			                        	if(url.equals(absHref)|| Character.isUpperCase(absHref.charAt(0))
			                        			|| absHref.indexOf("wikipedia.org")== -1 || absHref.charAt(0) == '(')
			                        	{
			                        		continue;
			                        	}
			                        	else if(map.containsValue(absHref))
			                        	{
			                        		done = true;
			                        	 	done2 = true;
			                        	 	break;
			                        	}
			                        	else if(absHref.equals("https://en.wikipedia.org/wiki/Philosophy"))
			                        	{
			                        		done = true;
			                        	 	done2 = true;
			                        	 	success = true;
			                        	 	map.put(i, absHref);
			                        	 	i++;
			                        	 	break;
			                        	}
			                        	else
			                        	{
			                        		done2 = true;
			                        	 	map.put(i, absHref);
			                        	 	i++;
			                        	 	url = absHref;
			                       			break;
			                        	}
		                        	}
		                        }
	        				}
	        			}
	                }
	        		if (k < paragraphs.size())
	        		{
		        		firstPara = paragraphs.get(k);
		        		k++;
	        		}
	        		else
	        		{
	        			done2 = true;
	        			done = true;
	        		}
            	}
            }
            
            Set s = map.entrySet();
            Iterator it = s.iterator();
            while(it.hasNext()) 
            {
            	Map.Entry ent = (Map.Entry)it.next();
                System.out.print(ent.getValue() + "\n");
            }
            
			if(success)
			{
				System.out.println("Success");
			}
			else
			{
				System.out.println("Failure");
			}
        }
        
		/*String url = "https://en.wikipedia.org/wiki/Java_(programming_language)";
		Elements paragraphs = wf.fetchWikipedia(url);

		Element firstPara = paragraphs.get(0);
		
		Iterable<Node> iter = new WikiNodeIterable(firstPara);
		for (Node node: iter) {
			if (node instanceof TextNode) {
				//System.out.print(node);
				//System.out.println("\n======BREAK=====\n");
			}
			else
			{
				Element e = (Element)node;
				String tName = e.tagName();
				System.out.print(tName + "\n");
				if(tName.equals("A")||tName.equals("a"))
                {
				String absHref = e.attr("abs:href");
				//System.out.print(absHref);
                }
			}
        }*/

        // the following throws an exception so the test fails
        // until you update the code
        //String msg = "Complete this lab by adding your code and removing this statement.";
        //throw new UnsupportedOperationException(msg);
	}
}
