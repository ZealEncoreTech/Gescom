package com.zeal.gov.kar.gescom.xmlbuzz;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.zeal.gov.kar.gescom.xmlmodel.Item;

public class CustomMapper implements  Converter  {

	@Override
	public boolean canConvert(Class clazz) {
		// TODO Auto-generated method stub
		 return clazz.equals(Item.class);
	}

	@Override
	public void marshal(Object obj, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		// TODO Auto-generated method stub
		Item item=(Item) obj;
		
		
		if(!item.itemslno.equalsIgnoreCase(""))
		{
			 writer.startNode("itemslno");
             context.convertAnother(item.itemslno);
             writer.endNode();
		}
		if(!item.itemnumber.equalsIgnoreCase(""))
		{
			 writer.startNode("itemnumber");
             context.convertAnother(item.itemnumber);
             writer.endNode();
		}
		if(!item.itemnumberdisplay.equalsIgnoreCase(""))
		{
			 writer.startNode("itemnumberdisplay");
             context.convertAnother(item.itemnumberdisplay);
             writer.endNode();
		}
		
		if(!item.itemtypeid.equalsIgnoreCase(""))
		{
			 writer.startNode("itemtypeid");
             context.convertAnother(item.itemtypeid);
             writer.endNode();
		}
		
		if(!item.fixed.equalsIgnoreCase(""))
		{
			 writer.startNode("fixed");
             context.convertAnother(item.fixed);
             writer.endNode();
		}
		if(!item.constant.equalsIgnoreCase(""))
		{
			 writer.startNode("constant");
             context.convertAnother(item.constant);
             writer.endNode();
		}
		if(!item.constantvalue.equalsIgnoreCase(""))
		{
			 writer.startNode("constantvalue");
             context.convertAnother(item.constantvalue);
             writer.endNode();
		}
		if(!item.formula.equalsIgnoreCase(""))
		{
			 writer.startNode("formula");
             context.convertAnother(item.formula);
             writer.endNode();
		}
		if(!item.amountquantity.equalsIgnoreCase(""))
		{
			 writer.startNode("amountquantity");
             context.convertAnother(item.amountquantity);
             writer.endNode();
		}
		if(!item.isInt.equalsIgnoreCase(""))
		{
			 writer.startNode("isInt");
             context.convertAnother(item.isInt);
             writer.endNode();
		}
		if(!item.qunatity.equalsIgnoreCase(""))
		{
			 writer.startNode("qunatity");
             context.convertAnother(item.qunatity);
             writer.endNode();
		}
		if(!item.decRound.equalsIgnoreCase(""))
		{
			 writer.startNode("decRound");
             context.convertAnother(item.decRound);
             writer.endNode();
		}
		
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
