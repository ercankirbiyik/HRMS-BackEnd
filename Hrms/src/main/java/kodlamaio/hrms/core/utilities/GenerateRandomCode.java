package kodlamaio.hrms.core.utilities;

import java.util.Random;

public class GenerateRandomCode {
	public String create() {
		int leftLimit = 97;
		int rightLimit = 122;
		int targetStringLength = 30;
		Random random = new Random();
		
		String generatedString = random.ints(leftLimit, rightLimit+1)
				.limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		return generatedString;
	}

}
