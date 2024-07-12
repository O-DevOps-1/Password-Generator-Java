import java.util.Random;
import java.util.Scanner;

public class ComplexPasswordGenerator {

  private static final String UPPERCASE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final String LOWERCASE_CHARS = "abcdefghijklmnopqrstuvwxyz";
  private static final String DIGIT_CHARS = "0123456789";
  private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+[]{};':\"\\|,.<>/?";

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // Minimum and maximum password length
    int minLength, maxLength;
    do {
      System.out.print("Enter minimum password length (8 or higher recommended): ");
      minLength = scanner.nextInt();
    } while (minLength < 8);
    maxLength = getValidMaxLength(scanner, minLength); // Use helper method

    boolean[] includeCharSets = getIncludeCharSets(scanner); // Use helper method

    // Password generation options
    boolean useAmbiguousChars = getYesNoInput(scanner, "Exclude ambiguous characters (e.g., 0, O, 1, l, I) (y/n)? ");
    boolean useSequentialChars = getYesNoInput(scanner, "Exclude sequential characters (e.g., abc, 123) (y/n)? ");

    String password = generatePassword(minLength, maxLength, includeCharSets, useAmbiguousChars, useSequentialChars);
    System.out.println("Your generated password is: " + password);

    // Password strength indicator
    int passwordStrength = getPasswordStrength(password);
    System.out.println("Password strength: " + getPasswordStrengthIndicator(passwordStrength));
  }

  private static int getValidMaxLength(Scanner scanner, int minLength) {
    do {
      System.out.print("Enter maximum password length (optional, press Enter for default): ");
      String maxLengthStr = scanner.nextLine();
      if (maxLengthStr.isEmpty()) {
        return minLength; // Default to min length if no max provided
        }
      try {
        int maxLength = Integer.parseInt(maxLengthStr);
        if (maxLength >= minLength) {
          return maxLength;
        } else {
          System.out.println("Maximum length must be greater than or equal to minimum length.");
        }
      } catch (NumberFormatException e) {
        System.out.println("Invalid input. Please enter a number.");
      }
    } while (true); // loop until a valiid maximum length is entered or Enter is pressed
    }

  private static boolean[] getIncludeCharSets(Scanner scanner) {
    boolean[] includeCharSets = new boolean[4]; 
    includeCharSets[0] = getYesNoInput(scanner, "Include uppercase letters (y/n)? ");

    includeCharSets[1] = getYesNoInput(scanner, "Include lowercase letters (y/n)? ");
    includeCharSets[2] = getYesNoInput(scanner, "Include numbers (y/n)? ");

    includeCharSets[3] = getYesNoInput(scanner, "Include special characters (y/n)? ");
    boolean atLeastOne = false;
    for (boolean include : includeCharSets) {
      if (include) {
        atLeastOne = true;
       break;}}
    if (!atLeastOne) {
      System.out.println("You must choose at least one character set for the password.");
      return getIncludeCharSets(scanner); }


    return includeCharSets; }



  private static boolean getYesNoInput(Scanner scanner, String prompt) {
    do {
      System.out.print(prompt);
      String input = scanner.nextLine();
      if (input.equalsIgnoreCase("y")) {
        return true;
      } else if (input.equalsIgnoreCase("n")) {
        return false;


      }} while (true); // loop until a valid yes/no answer is provided
  }

  public static String generatePassword(int minLength, int maxLength, boolean[] includeCharSets, boolean useAmbiguousChars, boolean useSequentialChars) {
    StringBuilder passwordBuilder = new StringBuilder();
    Random random = new Random();

    // Ensure at least one character from each included category
    for (int i = 0; i < includeCharSets.length; i++) {
      if (includeCharSets[i]) {
        String charSet;
        switch (i) {
          case 0:  charSet = UPPERCASE_CHARS; break;
          case 1:  charSet = LOWERCASE_CHARS; break;
          case 2:  charSet = DIGIT_CHARS; break;
          case 3:  charSet = SPECIAL_CHARS; break;
          default: charSet = "";
         break;}


        if (useAmbiguousChars) {
          charSet = removeAmbiguousChars(charSet);
        }
        passwordBuilder.append(getRandomChar(charSet, random));
      
      }}

    // Fill the rest of the password with random characters
    while (passwordBuilder.length() < minLength) {
      String allCharSets = "";
      for (int i = 0; i < includeCharSets.length; i++) {
        if (includeCharSets[i]) {
          switch (i) {
            case 0:  allCharSets += UPPERCASE_CHARS; break;
            case 1:  allCharSets += LOWERCASE_CHARS; break;
            case 2:  allCharSets += DIGIT_CHARS; break;
            case 3:  allCharSets += SPECIAL_CHARS; break;}}}

      if (useAmbiguousChars) {
        allCharSets = removeAmbiguousChars(allCharSets);
      }
      passwordBuilder.append(getRandomChar(allCharSets, random));}
    if (passwordBuilder.length() > maxLength) {
      passwordBuilder.setLength(maxLength);}

    // avoiding sequential characters
    if (useSequentialChars) {
      char[] passwordArray = passwordBuilder.toString().toCharArray();
      shuffleArray(passwordArray, random);
      passwordBuilder = new StringBuilder(new String(passwordArray));
    }

    return passwordBuilder.toString(); }

  private static String removeAmbiguousChars(String charSet) {
    // remove ambiguous characters 
    charSet = charSet.replace("0", "").replace("O", "").replace("1", "").replace("l", "").replace("I", "");
    return charSet;
  }

  private static char getRandomChar(String charSet, Random random) {
    // gets a random character from the character set
    int randomIndex = random.nextInt(charSet.length());
    return charSet.charAt(randomIndex);
  }

  private static void shuffleArray(char[] array, Random random) {
    // Shuffle the array using the Fisher-Yates shuffle algorithm
    for (int i = array.length - 1; i > 0; i--) {
      int randomIndex = random.nextInt(i + 1);
      char temp = array[i];
      array[i] = array[randomIndex];
      array[randomIndex] = temp;
    }
  }

  private  static int getPasswordStrength(String password) {
    int strength = 0;

    if (password.matches(".*[A-Z].*")) {
      strength++; }

    if (password.matches(".*[a-z].*")) {
      strength++;}

    if (password.matches(".*\\d.*")) {
      strength++;}

    if (password.matches(".*[!@#$%^&*()-_=+[]{};':\"\\|,.<>/?].*")) {
      strength++;   }

    if (password.length() >= 12) {
      strength++;
    }

    return strength;}

  private static String getPasswordStrengthIndicator(int strength) {
    switch (strength) {
      case 1: return "Very Weak";
      case 2: return "Weak";
      case 3: return "Medium";
      case 4: return "Strong";
      case 5: return "Very Strong";
      default: return "Unknown"; }}}