/**
 *  DBEXEL is a Database Backed & Web-Based Worksheet and chart management program. 
 *  It has been influenced by Excel.
 *  For questions or suggestions please contact the developper at ( Development@Gandomi.com )
 *  Copyright (C) 2011 Baubak Gandomi   
 *
 *	This file is part of the application DBEXEL
 *
 *   DBEXEL is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   DBEXEL is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package dbexel.system.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opensymphony.xwork2.util.ArrayUtils;

import dbexel.presentation.actions.AttributeTypes;

/**
 * @author gandomi
 * 
 */
public class DBExelTools {

	/**
	 * This method parses the given string and returns a Double. If the string
	 * could not be parsed it should
	 * 
	 * @param in_string
	 * @return
	 */
	public static Double parseStringToDouble(String in_string) {
		String r_doubleString = in_string;
		String l_decimalSeparator = FileUtils
				.fetchProperty("dbexel.internal.decimalSeparator");

		if (in_string.length() > 0) {
			// Remove all spaces
			r_doubleString = r_doubleString.replaceAll(" ", "");

			// char l_currencyVal ;

			for (char lt_char : r_doubleString.toCharArray()) {

				if (Character.getType(lt_char) != Character.DECIMAL_DIGIT_NUMBER) {

					if (".,".indexOf(lt_char) >= 0) {
						break;
					}

					// case Character.CURRENCY_SYMBOL : l_currencyVal=lt_char;
					// break;
					return null;
				}
			}

			// replace all ambiguous comma separators
			r_doubleString = r_doubleString.replace(",", l_decimalSeparator);

			// replace all ambiguous comma separators
			r_doubleString = r_doubleString.replace(".", l_decimalSeparator);
		} else {
			return null;
		}

		return Double.parseDouble(r_doubleString);
	}

	/**
	 * This method returns the type of the given string
	 * 
	 * @param in_string
	 * @return
	 */
	public static AttributeTypes identifyType(String in_string) {
		if (parseStringToDouble(in_string) != null) {
			return AttributeTypes.Number;
		}
		return AttributeTypes.Text;
	}

	/**
	 * This method returns the most common types among the given list
	 * 
	 * @param in_stringList
	 * @return
	 */
	public static AttributeTypes identifyType(List<String> in_stringList) {
		Map<AttributeTypes, Integer> l_sizeMap = new HashMap<AttributeTypes, Integer>();

		// Fill map with the number of occurences of each type
		for (String lt_stringItem : in_stringList) {
			AttributeTypes lt_typeA = DBExelTools.identifyType(lt_stringItem);
			if (!l_sizeMap.containsKey(lt_typeA)) {
				l_sizeMap.put(lt_typeA, 1);
			} else {
				l_sizeMap.put(lt_typeA, l_sizeMap.get(lt_typeA) + 1);
			}
		}

		// Select in the map the type that is most common
		AttributeTypes l_greatestType = AttributeTypes.Text;
		Integer l_greatestSize = 0;

		for (AttributeTypes lt_typeB : l_sizeMap.keySet()) {
			if (l_sizeMap.get(lt_typeB) > l_greatestSize) {
				l_greatestType = lt_typeB;
				l_greatestSize = l_sizeMap.get(lt_typeB);
			}
		}
		
		//If the returned value is equal to text always return text.
		if (l_sizeMap.get(AttributeTypes.Text) == l_greatestSize) {
			return AttributeTypes.Text;
		}
		
		return l_greatestType;
	}
	
	/**
	 * This method returns the most common types among the given list
	 * 
	 * @param in_stringSet
	 * @return
	 */
	public static AttributeTypes identifyType(Set<String> in_stringSet) {
		List<String> l_list = new ArrayList<String>();
		l_list.addAll(in_stringSet);
		
		return identifyType(l_list);
	}

}
