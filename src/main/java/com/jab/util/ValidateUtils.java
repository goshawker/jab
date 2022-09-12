package com.jab.util;

import java.text.ParseException;
import java.util.*;



/**
*
* @Author: goshawker@yeah.net
* @Description:
* @Date: 2022/9/12 11:00
* @Version: 1.0
*/
public class ValidateUtils {

  /**
   * The Constant ContiguousUSStateCodes.
   */
  public static final String __ContiguousUSStateCodes = "AL|AZ|AR|CA|CO|CT|DE|DC|FL|GA|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY";

  /**
   * The Constant SSNDelimiters.
   */
  public static final String __SSNDelimiters = "- ";

  /**
   * The Constant USStateCodeDelimiter.
   */
  public static final String __USStateCodeDelimiter = "|";

  /**
   * The Constant USStateCodes.
   */
  public static final String __USStateCodes = "AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY|AE|AA|AE|AE|AP";

  /**
   * The Constant ZipCodeDelimeter.
   */
  public static final String __ZipCodeDelimeter = "-";

  /**
   * The Constant ZipCodeDelimiters.
   */
  public static final String __ZipCodeDelimiters = "-";

  /**
   * The Constant creditCardDelimiters.
   */
  public static final String __creditCardDelimiters = " -";

  /**
   * The Constant daysInMonth.
   */
  public static final int[] __daysInMonth = {31, 29, 31, 30, 31, 30, 31, 31,
          30, 31, 30, 31};

  /**
   * The Constant decimalPointDelimiter.
   */
  public static final String __decimalPointDelimiter = ".";

  /**
   * The Constant defaultEmptyOK.
   */
  public static final boolean __defaultEmptyOK = true;

  /**
   * The Constant digits.
   */
  public static final String __digits = "0123456789";

  /**
   * The Constant digitsInSocialSecurityNumber.
   */
  public static final int __digitsInSocialSecurityNumber = 9;

  /**
   * The Constant digitsInUSPhoneAreaCode.
   */
  public static final int __digitsInUSPhoneAreaCode = 3;

  /**
   * The Constant digitsInUSPhoneMainNumber.
   */
  public static final int __digitsInUSPhoneMainNumber = 7;

  /**
   * The Constant digitsInUSPhoneNumber.
   */
  public static final int __digitsInUSPhoneNumber = 10;

  /**
   * The Constant digitsInZipCode1.
   */
  public static final int __digitsInZipCode1 = 5;

  /**
   * The Constant digitsInZipCode2.
   */
  public static final int __digitsInZipCode2 = 9;

  /**
   * The Constant isAnyCardMsg.
   */
  public static final String __isAnyCardMsg = "The credit card number is not a valid card number.";

  /**
   * The Constant isContiguousStateCodeMsg.
   */
  public static final String __isContiguousStateCodeMsg = "The State Code must be a valid two character U.S. state abbreviation for one of the 48 contiguous United States (like CA for California).";

  /**
   * The Constant isContiguousZipCodeMsg.
   */
  public static final String __isContiguousZipCodeMsg = "Zip Code is not a valid Zip Code for one of the 48 contiguous United States .";

  /**
   * The Constant isCreditCardPrefixMsg.
   */
  public static final String __isCreditCardPrefixMsg = " is not a valid ";

  /**
   * The Constant isCreditCardSuffixMsg.
   */
  public static final String __isCreditCardSuffixMsg = " credit card number.";

  /**
   * The Constant isDateAfterToday.
   */
  public static final String __isDateAfterToday = "The Date must be a valid date after today, and formed like: MM/YY, MM/YYYY, MM/DD/YY, or MM/DD/YYYY.";

  /**
   * The Constant isDateMsg.
   */
  public static final String __isDateMsg = "The Date must be a valid date formed like: MM/YY, MM/YYYY, MM/DD/YY, or MM/DD/YYYY.";

  /**
   * The Constant isDatePrefixMsg.
   */
  public static final String __isDatePrefixMsg = "The Day, Month, and Year for ";

  /**
   * The Constant isDateSuffixMsg.
   */
  public static final String __isDateSuffixMsg = " do not form a valid date.  Please reenter them now.";

  /**
   * The Constant isDayMsg.
   */
  public static final String __isDayMsg = "The Day must be a day number between 1 and 31. ";

  /**
   * The Constant isEmailMsg.
   */
  public static final String __isEmailMsg = "The Email must be a valid email address(like john@email.com). Please re-enter it now.";

  /**
   * The Constant isFloatMsg.
   */
  public static final String __isFloatMsg = "The Number must be a valid unsigned decimal number.";

  /**
   * The Constant isHourMsg.
   */
  public static final String __isHourMsg = "The Hour must be a number between 0 and 23.";

  /**
   * The Constant isIntegerMsg.
   */
  public static final String __isIntegerMsg = "The Number must be a valid unsigned whole decimal number.";

  /**
   * The Constant isInternationalPhoneNumberMsg.
   */
  public static final String __isInternationalPhoneNumberMsg = "The World Phone must be a valid international phone number.";

  /**
   * The Constant isLongMsg.
   */
  public static final String __isLongMsg = "The Number must be a valid unsigned whole decimal number.";

  /**
   * The Constant isMinuteMsg.
   */
  public static final String __isMinuteMsg = "The Hour must be a number between 0 and 59.";

  /**
   * The Constant isMonthMsg.
   */
  public static final String __isMonthMsg = "The Month must be a month number between 1 and 12. ";

  /**
   * The Constant isNotEmptyMsg.
   */
  public static final String __isNotEmptyMsg = "This field cannot be empty, please enter a value.";

  /**
   * The Constant isSSNMsg.
   */
  public static final String __isSSNMsg = "The SSN must be a 9 digit U.S. social security number(like 123-45-6789).";

  /**
   * The Constant isSecondMsg.
   */
  public static final String __isSecondMsg = "The Hour must be a number between 0 and 59.";

  /**
   * The Constant isSignedDoubleMsg.
   */
  public static final String __isSignedDoubleMsg = "The Number must be a valid signed decimal number.";

  /**
   * The Constant isSignedFloatMsg.
   */
  public static final String __isSignedFloatMsg = "The Number must be a valid signed decimal number.";

  /**
   * The Constant isSignedIntegerMsg.
   */
  public static final String __isSignedIntegerMsg = "The Number must be a valid signed whole decimal number.";

  /**
   * The Constant isSignedLongMsg.
   */
  public static final String __isSignedLongMsg = "The Number must be a valid signed whole decimal number.";

  /**
   * The Constant isStateCodeMsg.
   */
  public static final String __isStateCodeMsg = "The State Code must be a valid two character U.S. state abbreviation(like CA for California).";

  /**
   * The Constant isTimeMsg.
   */
  public static final String __isTimeMsg = "The Time must be a valid time formed like: HH:MM or HH:MM:SS.";

  /**
   * The Constant isUSPhoneAreaCodeMsg.
   */
  public static final String __isUSPhoneAreaCodeMsg = "The Phone Number Area Code must be 3 digits.";

  /**
   * The Constant isUSPhoneMainNumberMsg.
   */
  public static final String __isUSPhoneMainNumberMsg = "The Phone Number must be 7 digits.";

  /**
   * The Constant isUSPhoneMsg.
   */
  public static final String __isUSPhoneMsg = "The US Phone must be a 10 digit U.S. phone number(like 415-555-1212).";

  /**
   * The Constant isYearMsg.
   */
  public static final String __isYearMsg = "The Year must be a 2 or 4 digit year number. ";

  /**
   * The Constant isZipCodeMsg.
   */
  public static final String __isZipCodeMsg = "The ZIP Code must be a 5 or 9 digit U.S. ZIP Code(like 94043).";

  /**
   * The Constant letters.
   */
  public static final String __letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * The Constant lowercaseLetters.
   */
  public static final String __lowercaseLetters = "abcdefghijklmnopqrstuvwxyz";

  /**
   * The Constant phoneNumberDelimiters.
   */
  public static final String __phoneNumberDelimiters = "()- ";

  /**
   * The Constant uppercaseLetters.
   */
  public static final String __uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * The Constant validSSNChars.
   */
  public static final String __validSSNChars = "0123456789- ";

  /**
   * The Constant validUSPhoneChars.
   */
  public static final String __validUSPhoneChars = "0123456789()- ";

  /**
   * The Constant validWorldPhoneChars.
   */
  public static final String __validWorldPhoneChars = "0123456789()- +";

  /**
   * The Constant validZipCodeChars.
   */
  public static final String __validZipCodeChars = "0123456789-";

  /**
   * The Constant whitespace.
   */
  public static final String __whitespace = " \t\n\r";

  /**
   * Are equal.
   *
   * @param obj  the obj
   * @param obj2 the obj2
   * @return true, if successful
   */
  public static boolean areEqual(Object obj, Object obj2) {
    if (obj == null) {
      return obj2 == null;
    }
    return obj.equals(obj2);
  }

  /**
   * Char in string.
   *
   * @param c the c
   * @param s the s
   * @return true, if successful
   */
  public static boolean charInString(char c, String s) {
    return s.indexOf(c) != -1;
  }

  /**
   * Days in february.
   *
   * @param year the year
   * @return the int
   */
  public static int daysInFebruary(int year) {
    return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0)) ? 29
            : 28;
  }

  /**
   * Gets the card type.
   *
   * @param ccPassed the cc passed
   * @return the card type
   */
  public static String getCardType(String ccPassed) {
    if (isEmpty(ccPassed))
      return "Unknown";
    String cc = stripCharsInBag(ccPassed, " -");

    if (!isCreditCard(cc))
      return "Unknown";

    if (isMasterCard(cc))
      return "MasterCard";
    if (isVisa(cc))
      return "Visa";
    if (isAmericanExpress(cc))
      return "AmericanExpress";
    if (isDinersClub(cc))
      return "DinersClub";
    if (isDiscover(cc))
      return "Discover";
    if (isEnRoute(cc))
      return "EnRoute";
    if (isJCB(cc))
      return "JCB";
    return "Unknown";
  }

  /**
   * Checks if is alphabetic.
   *
   * @param s the s
   * @return true, if is alphabetic
   */
  public static boolean isAlphabetic(String s) {
    if (isEmpty(s))
      return true;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!isLetter(c)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if is alphanumeric.
   *
   * @param s the s
   * @return true, if is alphanumeric
   */
  public static boolean isAlphanumeric(String s) {
    if (isEmpty(s))
      return true;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!isLetterOrDigit(c))
        return false;
    }
    return true;
  }

  /**
   * Checks if is american express.
   *
   * @param cc the cc
   * @return true, if is american express
   */
  public static boolean isAmericanExpress(String cc) {
    int firstdig = Integer.parseInt(cc.substring(0, 1));
    int seconddig = Integer.parseInt(cc.substring(1, 2));

    if ((cc.length() == 15) && (firstdig == 3)
            && ((seconddig == 4) || (seconddig == 7)))
      return isCreditCard(cc);
    return false;
  }

  /**
   * Checks if is any card.
   *
   * @param ccPassed the cc passed
   * @return true, if is any card
   */
  public static boolean isAnyCard(String ccPassed) {
    if (isEmpty(ccPassed))
      return true;

    String cc = stripCharsInBag(ccPassed, " -");

    if (!isCreditCard(cc))
      return false;

    return (isMasterCard(cc)) || (isVisa(cc)) || (isAmericanExpress(cc))
            || (isDinersClub(cc)) || (isDiscover(cc)) || (isEnRoute(cc))
            || (isJCB(cc));
  }

  /**
   * Checks if is card match.
   *
   * @param cardType         the card type
   * @param cardNumberPassed the card number passed
   * @return true, if is card match
   */
  public static boolean isCardMatch(String cardType, String cardNumberPassed) {
    if (isEmpty(cardType))
      return true;
    if (isEmpty(cardNumberPassed))
      return true;
    String cardNumber = stripCharsInBag(cardNumberPassed, " -");

    if ((cardType.equalsIgnoreCase("VISA")) && (isVisa(cardNumber)))
      return true;
    if ((cardType.equalsIgnoreCase("MASTERCARD"))
            && (isMasterCard(cardNumber)))
      return true;
    if (((cardType.equalsIgnoreCase("AMERICANEXPRESS")) || (cardType
            .equalsIgnoreCase("AMEX"))) && (isAmericanExpress(cardNumber)))
      return true;
    if ((cardType.equalsIgnoreCase("DISCOVER")) && (isDiscover(cardNumber)))
      return true;
    if ((cardType.equalsIgnoreCase("JCB")) && (isJCB(cardNumber)))
      return true;
    if (((cardType.equalsIgnoreCase("DINERSCLUB")) || (cardType
            .equalsIgnoreCase("DINERS"))) && (isDinersClub(cardNumber)))
      return true;
    if ((cardType.equalsIgnoreCase("CARTEBLANCHE"))
            && (isCarteBlanche(cardNumber)))
      return true;
    return (cardType.equalsIgnoreCase("ENROUTE"))
            && (isEnRoute(cardNumber));
  }

  /**
   * Checks if is carte blanche.
   *
   * @param cc the cc
   * @return true, if is carte blanche
   */
  public static boolean isCarteBlanche(String cc) {
    return isDinersClub(cc);
  }

  /**
   * Checks if is contiguous state code.
   *
   * @param s the s
   * @return true, if is contiguous state code
   */
  public static boolean isContiguousStateCode(String s) {
    if (isEmpty(s))
      return true;
    return ("AL|AZ|AR|CA|CO|CT|DE|DC|FL|GA|ID|IL|IN|IA|KS|KY|LA|ME|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|OH|OK|OR|PA|RI|SC|SD|TN|TX|UT|VT|VA|WA|WV|WI|WY"
            .indexOf(s) != -1) && (s.indexOf("|") == -1);
  }

  /**
   * Checks if is contiguous zip code.
   *
   * @param s the s
   * @return true, if is contiguous zip code
   */
  public static boolean isContiguousZipCode(String s) {
    boolean retval = false;
    if (isZipCode(s)) {
      if (isEmpty(s)) {
        retval = true;
      } else {
        String normalizedZip = s.substring(0, 5);
        int iZip = Integer.parseInt(normalizedZip);
        retval = ((iZip < 96701) || (iZip > 96898))
                && ((iZip < 99501) || (iZip > 99950));
      }
    }
    return retval;
  }

  /**
   * Checks if is credit card.
   *
   * @param stPassed the st passed
   * @return true, if is credit card
   */
  public static boolean isCreditCard(String stPassed) {
    if (isEmpty(stPassed))
      return true;
    String st = stripCharsInBag(stPassed, " -");

    int sum = 0;
    int mul = 1;
    int l = st.length();
    if (l > 19)
      return false;
    for (int i = 0; i < l; i++) {
      String digit = st.substring(l - i - 1, l - i);
      int tproduct = 0;
      try {
        tproduct = Integer.parseInt(digit, 10) * mul;
      } catch (Exception e) {
        return false;
      }
      if (tproduct >= 10)
        sum += tproduct % 10 + 1;
      else
        sum += tproduct;
      if (mul == 1)
        mul++;
      else {
        mul--;
      }

    }

    return sum % 10 == 0;
  }

  /**
   * Checks if is date.
   *
   * @param date the date
   * @return true, if is date
   */
  public static boolean isDate(String date) {
    if (isEmpty(date))
      return true;
    int dateSlash1 = date.indexOf("/");
    int dateSlash2 = date.lastIndexOf("/");
    if ((dateSlash1 <= 0) || (dateSlash1 == dateSlash2))
      return false;
    String month = date.substring(0, dateSlash1);
    String day = date.substring(dateSlash1 + 1, dateSlash2);
    String year = date.substring(dateSlash2 + 1);

    return isDate(year, month, day);
  }

  /**
   * Checks if is date.
   *
   * @param year  the year
   * @param month the month
   * @param day   the day
   * @return true, if is date
   */
  public static boolean isDate(String year, String month, String day) {
    if ((!isYear(year)) || (!isMonth(month)) || (!isDay(day)))
      return false;
    int intYear = Integer.parseInt(year);
    int intMonth = Integer.parseInt(month);
    int intDay = Integer.parseInt(day);
    if (intDay > __daysInMonth[(intMonth - 1)])
      return false;
    return (intMonth != 2) || (intDay <= daysInFebruary(intYear));
  }

  /**
   * Checks if is date after today.
   *
   * @param date the date
   * @return true, if is date after today
   * @throws ParseException the parse exception
   */
  public static boolean isDateAfterToday(String date) throws ParseException {
    if (isEmpty(date))
      return true;
    int dateSlash1 = date.indexOf("/");
    int dateSlash2 = date.lastIndexOf("/");
    if (dateSlash1 <= 0)
      return false;
    Date passed = null;
    if (dateSlash1 == dateSlash2) {
      String month = date.substring(0, dateSlash1);
      String day = "28";
      String year = date.substring(dateSlash1 + 1);
      if (!isDate(year, month, day))
        return false;
      try {
        int monthInt = Integer.parseInt(month);
        int yearInt = Integer.parseInt(year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(yearInt, monthInt - 1, 0, 0, 0, 0);
        calendar.add(2, 1);
        passed = new Date(calendar.getTime().getTime());
      } catch (Exception e) {
        passed = null;
      }
    } else {
      String month = date.substring(0, dateSlash1);
      String day = date.substring(dateSlash1 + 1, dateSlash2);
      String year = date.substring(dateSlash2 + 1);
      if (!isDate(year, month, day))
        return false;
      passed = DateUtils.parseDateTime(year + "-" + "month" + "day");
    }
    Date now = new Date();
    if (passed != null) {
      return passed.after(now);
    }
    return false;
  }

  /**
   * Checks if is day.
   *
   * @param s the s
   * @return true, if is day
   */
  public static boolean isDay(String s) {
    if (isEmpty(s))
      return true;
    return isIntegerInRange(s, 1, 31);
  }

  /**
   * Checks if is digit.
   *
   * @param c the c
   * @return true, if is digit
   */
  public static boolean isDigit(char c) {
    return Character.isDigit(c);
  }

  /**
   * Checks if is diners club.
   *
   * @param cc the cc
   * @return true, if is diners club
   */
  public static boolean isDinersClub(String cc) {
    int firstdig = Integer.parseInt(cc.substring(0, 1));
    int seconddig = Integer.parseInt(cc.substring(1, 2));
    if ((cc.length() == 14) && (firstdig == 3)
            && ((seconddig == 0) || (seconddig == 6) || (seconddig == 8)))
      return isCreditCard(cc);
    return false;
  }

  /**
   * Checks if is discover.
   *
   * @param cc the cc
   * @return true, if is discover
   */
  public static boolean isDiscover(String cc) {
    String first4digs = cc.substring(0, 4);
    if ((cc.length() == 16) && (first4digs.equals("6011")))
      return isCreditCard(cc);
    return false;
  }

  /**
   * Checks if is email.
   *
   * @param s the s
   * @return true, if is email
   */
  public static boolean isEmail(String s) {
    if (isEmpty(s))
      return true;
    if (isWhitespace(s))
      return false;
    int i = 1;
    int sLength = s.length();
    while ((i < sLength) && (s.charAt(i) != '@'))
      i++;
    return (i < sLength - 1) && (s.charAt(i) == '@');
  }

  /**
   * Checks if is empty.
   *
   * @param c the c
   * @return true, if is empty
   */
  public static boolean isEmpty(Collection c) {
    return (c == null) || (c.size() == 0);
  }

  /**
   * Checks if is empty.
   *
   * @param s the s
   * @return true, if is empty
   */
  public static boolean isEmpty(String s) {
    return (s == null) || (s.length() == 0);
  }

  /**
   * Checks if is en route.
   *
   * @param cc the cc
   * @return true, if is en route
   */
  public static boolean isEnRoute(String cc) {
    String first4digs = cc.substring(0, 4);
    if ((cc.length() == 15)
            && ((first4digs.equals("2014")) || (first4digs.equals("2149"))))
      return isCreditCard(cc);
    return false;
  }

  /**
   * Checks if is float.
   *
   * @param s the s
   * @return true, if is float
   */
  public static boolean isFloat(String s) {
    if (isEmpty(s))
      return true;
    boolean seenDecimalPoint = false;
    if (s.startsWith("."))
      return false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == ".".charAt(0)) {
        if (!seenDecimalPoint)
          seenDecimalPoint = true;
        else
          return false;
      } else if (!isDigit(c))
        return false;
    }

    return true;
  }

  /**
   * Checks if is hour.
   *
   * @param s the s
   * @return true, if is hour
   */
  public static boolean isHour(String s) {
    if (isEmpty(s))
      return true;
    return isIntegerInRange(s, 0, 23);
  }

  /**
   * Checks if is integer.
   *
   * @param s the s
   * @return true, if is integer
   */
  public static boolean isInteger(String s) {
    if (isEmpty(s))
      return true;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (!isDigit(c))
        return false;

    }

    return true;
  }

  /**
   * Checks if is integer in range.
   *
   * @param s the s
   * @param a the a
   * @param b the b
   * @return true, if is integer in range
   */
  public static boolean isIntegerInRange(String s, int a, int b) {
    if (isEmpty(s))
      return true;
    if (!isSignedInteger(s))
      return false;
    int num = Integer.parseInt(s);
    return (num >= a) && (num <= b);
  }

  /**
   * Checks if is international phone number.
   *
   * @param s the s
   * @return true, if is international phone number
   */
  public static boolean isInternationalPhoneNumber(String s) {
    if (isEmpty(s))
      return true;
    String normalizedPhone = stripCharsInBag(s, "()- ");
    return isPositiveInteger(normalizedPhone);
  }

  /**
   * Checks if is jcb.
   *
   * @param cc the cc
   * @return true, if is jcb
   */
  public static boolean isJCB(String cc) {
    String first4digs = cc.substring(0, 4);

    if ((cc.length() == 16)
            && ((first4digs.equals("3088")) || (first4digs.equals("3096"))
            || (first4digs.equals("3112"))
            || (first4digs.equals("3158"))
            || (first4digs.equals("3337")) || (first4digs
            .equals("3528")))) {
      return isCreditCard(cc);
    }
    return false;
  }

  /**
   * Checks if is letter.
   *
   * @param c the c
   * @return true, if is letter
   */
  public static boolean isLetter(char c) {
    return Character.isLetter(c);
  }

  /**
   * Checks if is letter or digit.
   *
   * @param c the c
   * @return true, if is letter or digit
   */
  public static boolean isLetterOrDigit(char c) {
    return Character.isLetterOrDigit(c);
  }

  /**
   * Checks if is master card.
   *
   * @param cc the cc
   * @return true, if is master card
   */
  public static boolean isMasterCard(String cc) {
    int firstdig = Integer.parseInt(cc.substring(0, 1));
    int seconddig = Integer.parseInt(cc.substring(1, 2));

    if ((cc.length() == 16) && (firstdig == 5) && (seconddig >= 1)
            && (seconddig <= 5))
      return isCreditCard(cc);
    return false;
  }

  /**
   * Checks if is minute.
   *
   * @param s the s
   * @return true, if is minute
   */
  public static boolean isMinute(String s) {
    if (isEmpty(s))
      return true;
    return isIntegerInRange(s, 0, 59);
  }

  /**
   * Checks if is month.
   *
   * @param s the s
   * @return true, if is month
   */
  public static boolean isMonth(String s) {
    if (isEmpty(s))
      return true;
    return isIntegerInRange(s, 1, 12);
  }

  /**
   * Checks if is negative integer.
   *
   * @param s the s
   * @return true, if is negative integer
   */
  public static boolean isNegativeInteger(String s) {
    if (isEmpty(s))
      return true;
    try {
      int temp = Integer.parseInt(s);
      return temp < 0;
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * Checks if is nonnegative integer.
   *
   * @param s the s
   * @return true, if is nonnegative integer
   */
  public static boolean isNonnegativeInteger(String s) {
    if (isEmpty(s))
      return true;
    try {
      int temp = Integer.parseInt(s);
      return temp >= 0;
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * Checks if is nonpositive integer.
   *
   * @param s the s
   * @return true, if is nonpositive integer
   */
  public static boolean isNonpositiveInteger(String s) {
    if (isEmpty(s))
      return true;
    try {
      int temp = Integer.parseInt(s);
      return temp <= 0;
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * Checks if is not empty.
   *
   * @param c the c
   * @return true, if is not empty
   */
  public static boolean isNotEmpty(Collection c) {
    return (c != null) && (c.size() > 0);
  }

  /**
   * Checks if is not empty.
   *
   * @param s the s
   * @return true, if is not empty
   */
  public static boolean isNotEmpty(String s) {
    return (s != null) && (s.length() > 0);
  }


  /**
   * Checks if is positive integer.
   *
   * @param s the s
   * @return true, if is positive integer
   */
  public static boolean isPositiveInteger(String s) {
    if (isEmpty(s))
      return true;
    try {
      long temp = Long.parseLong(s);
      return temp > 0L;
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * Checks if is ssn.
   *
   * @param s the s
   * @return true, if is ssn
   */
  public static boolean isSSN(String s) {
    if (isEmpty(s))
      return true;
    String normalizedSSN = stripCharsInBag(s, "- ");
    return (isInteger(normalizedSSN)) && (normalizedSSN.length() == 9);
  }

  /**
   * Checks if is second.
   *
   * @param s the s
   * @return true, if is second
   */
  public static boolean isSecond(String s) {
    if (isEmpty(s))
      return true;
    return isIntegerInRange(s, 0, 59);
  }

  /**
   * Checks if is signed double.
   *
   * @param s the s
   * @return true, if is signed double
   */
  public static boolean isSignedDouble(String s) {
    if (isEmpty(s))
      return true;
    try {
      double temp = Double.parseDouble(s);
      return true;
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * Checks if is signed float.
   *
   * @param s the s
   * @return true, if is signed float
   */
  public static boolean isSignedFloat(String s) {
    if (isEmpty(s))
      return true;
    try {
      float temp = Float.parseFloat(s);
      return temp <= 0.0F;
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * Checks if is signed integer.
   *
   * @param s the s
   * @return true, if is signed integer
   */
  public static boolean isSignedInteger(String s) {
    if (isEmpty(s))
      return true;
    try {
      int temp = Integer.parseInt(s);
      return true;
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * Checks if is signed long.
   *
   * @param s the s
   * @return true, if is signed long
   */
  public static boolean isSignedLong(String s) {
    if (isEmpty(s))
      return true;
    try {
      long temp = Long.parseLong(s);
      return true;
    } catch (Exception e) {
    }
    return false;
  }

  /**
   * Checks if is state code.
   *
   * @param s the s
   * @return true, if is state code
   */
  public static boolean isStateCode(String s) {
    if (isEmpty(s))
      return true;
    return ("AL|AK|AS|AZ|AR|CA|CO|CT|DE|DC|FM|FL|GA|GU|HI|ID|IL|IN|IA|KS|KY|LA|ME|MH|MD|MA|MI|MN|MS|MO|MT|NE|NV|NH|NJ|NM|NY|NC|ND|MP|OH|OK|OR|PW|PA|PR|RI|SC|SD|TN|TX|UT|VT|VI|VA|WA|WV|WI|WY|AE|AA|AE|AE|AP"
            .indexOf(s) != -1) && (s.indexOf("|") == -1);
  }

  /**
   * Checks if is time.
   *
   * @param time the time
   * @return true, if is time
   */
  public static boolean isTime(String time) {
    if (isEmpty(time))
      return true;

    int timeColon1 = time.indexOf(":");
    int timeColon2 = time.lastIndexOf(":");

    if (timeColon1 <= 0)
      return false;
    String hour = time.substring(0, timeColon1);
    String minute;
    String second;
    if (timeColon1 == timeColon2) {
      minute = time.substring(timeColon1 + 1);
      second = "0";
    } else {
      minute = time.substring(timeColon1 + 1, timeColon2);
      second = time.substring(timeColon2 + 1);
    }
    return isTime(hour, minute, second);
  }

  /**
   * Checks if is time.
   *
   * @param hour   the hour
   * @param minute the minute
   * @param second the second
   * @return true, if is time
   */
  public static boolean isTime(String hour, String minute, String second) {
    return (isHour(hour)) && (isMinute(minute)) && (isSecond(second));
  }

  /**
   * Checks if is uS phone area code.
   *
   * @param s the s
   * @return true, if is uS phone area code
   */
  public static boolean isUSPhoneAreaCode(String s) {
    if (isEmpty(s))
      return true;
    String normalizedPhone = stripCharsInBag(s, "()- ");

    return (isInteger(normalizedPhone)) && (normalizedPhone.length() == 3);
  }

  /**
   * Checks if is uS phone main number.
   *
   * @param s the s
   * @return true, if is uS phone main number
   */
  public static boolean isUSPhoneMainNumber(String s) {
    if (isEmpty(s))
      return true;
    String normalizedPhone = stripCharsInBag(s, "()- ");

    return (isInteger(normalizedPhone)) && (normalizedPhone.length() == 7);
  }

  /**
   * Checks if is uS phone number.
   *
   * @param s the s
   * @return true, if is uS phone number
   */
  public static boolean isUSPhoneNumber(String s) {
    if (isEmpty(s))
      return true;
    String normalizedPhone = stripCharsInBag(s, "()- ");

    return (isInteger(normalizedPhone)) && (normalizedPhone.length() == 10);
  }

  /**
   * Checks if is visa.
   *
   * @param cc the cc
   * @return true, if is visa
   */
  public static boolean isVisa(String cc) {
    if (((cc.length() == 16) || (cc.length() == 13))
            && (cc.charAt(0) == '4'))
      return isCreditCard(cc);
    return false;
  }

  /**
   * Checks if is whitespace.
   *
   * @param s the s
   * @return true, if is whitespace
   */
  public static boolean isWhitespace(String s) {
    if (isEmpty(s))
      return true;

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (" \t\n\r".indexOf(c) == -1)
        return false;
    }

    return true;
  }

  /**
   * Checks if is year.
   *
   * @param s the s
   * @return true, if is year
   */
  public static boolean isYear(String s) {
    if (isEmpty(s))
      return true;

    if (!isNonnegativeInteger(s))
      return false;
    return (s.length() == 2) || (s.length() == 4);
  }

  /**
   * Checks if is zip code.
   *
   * @param s the s
   * @return true, if is zip code
   */
  public static boolean isZipCode(String s) {
    if (isEmpty(s))
      return true;

    String normalizedZip = stripCharsInBag(s, "-");

    return (isInteger(normalizedZip))
            && ((normalizedZip.length() == 5) || (normalizedZip.length() == 9));
  }

  /**
   * Strip chars in bag.
   *
   * @param s   the s
   * @param bag the bag
   * @return the string
   */
  public static String stripCharsInBag(String s, String bag) {
    String returnString = "";

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (bag.indexOf(c) != -1)
        continue;
      returnString = returnString + c;
    }
    return returnString;
  }

  /**
   * Strip chars not in bag.
   *
   * @param s   the s
   * @param bag the bag
   * @return the string
   */
  public static String stripCharsNotInBag(String s, String bag) {
    String returnString = "";

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (bag.indexOf(c) == -1)
        continue;
      returnString = returnString + c;
    }
    return returnString;
  }

  /**
   * Strip initial whitespace.
   *
   * @param s the s
   * @return the string
   */
  public static String stripInitialWhitespace(String s) {
    int i = 0;

    while ((i < s.length()) && (charInString(s.charAt(i), " \t\n\r")))
      i++;
    return s.substring(i);
  }

  /**
   * Strip whitespace.
   *
   * @param s the s
   * @return the string
   */
  public static String stripWhitespace(String s) {
    return stripCharsInBag(s, " \t\n\r");
  }

  /**
   * 判断参数是否为NULL.
   *
   * @param src the src
   * @return the string
   */
  public static String sNull(Object src) {
    return src == null ? "" : src.toString();
  }


  /**
   * 判断参数是否不为null和不为空.
   *
   * @param src the src
   * @return true, if successful
   */
  public static boolean notEmpty(String src) {
    return src != null && !"".equals(src);
  }

  /**
   * 判断参数是否为null或空.
   *
   * @param __obj the obj
   * @return true, if is null or empty
   */
  public static boolean isNullOrEmpty(Object __obj) {
    if (__obj == null)
      return true;

    if (__obj instanceof CharSequence)
      return ((CharSequence) __obj).length() == 0;

    if (__obj instanceof Collection)
      return ((Collection) __obj).isEmpty();

    if (__obj instanceof Map)
      return ((Map) __obj).isEmpty();
    if (__obj instanceof Object[]) {
      Object[] object = (Object[]) __obj;
      if (object.length == 0) {
        return true;
      }
      boolean empty = true;
      for (int i = 0; i < object.length; i++) {
        if (!isNullOrEmpty(object[i])) {
          empty = false;
          break;
        }
      }
      return empty;
    }
    return false;

  }

  /**
   * 判断参数是否是数字.
   *
   * @param src the src
   * @return true, if is numer
   */
  public static boolean isNumer(String src) {
    try {
      Integer.parseInt(src);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }

  }

  /**
   *
   * @param
   * @return boolean
   */
  public static boolean isWindows() {
    Properties prop = System.getProperties();
    String os = prop.getProperty("os.name");
    return os.toLowerCase().startsWith("win");
  }
}