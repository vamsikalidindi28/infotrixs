package com.infotrix.cc.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrencyConverter {

	private static Set<String> favoriteCurrencies = new HashSet<>();

	private static void addFavoriteCurrency() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter the currency code to add to favorites: ");
		try {
			String currencyCode = reader.readLine().toUpperCase();
			favoriteCurrencies.add(currencyCode);
			System.out.println(currencyCode + " has been added to your favorites.");
		} catch (IOException e) {
			System.out.println("Error reading input: " + e.getMessage());
		}
	}

	private static void viewFavoriteCurrencies() {
		System.out.println("Favorite Currencies:");
		for (String currencyCode : favoriteCurrencies) {
			System.out.println(currencyCode);
		}
	}

	private static void convertCurrency() throws IOException {
		HashMap<Integer, String> currencyCodes = new HashMap<Integer, String>();
		// Add currency Codes
		currencyCodes.put(1, "USD");
		currencyCodes.put(2, "CAD");
		currencyCodes.put(3, "EUR");
		currencyCodes.put(4, "HKD");
		currencyCodes.put(5, "INR");

		String fromCode, toCode;
		double amount;
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome to the Currency Converter!");

		System.out.println("Currency Converting From?");
		System.out.println(
				"1: USD(US Dollar)\t 2: CAD(Canadian Dollar)\t 3: EUR(Euro)\t 4: HKD (Hong Kong Dollar)\t 5. INR (Indian Rupees)");
		int fromCurrencyChoice = sc.nextInt();
		fromCode = currencyCodes.get(fromCurrencyChoice);

		System.out.println("Currency Converting To?");
		System.out.println(
				"1: USD(US Dollar)\t 2: CAD(Canadian Dollar)\t 3: EUR(Euro)\t 4: HKD (Hong Kong Dollar)\t 5. INR (Indian Rupees)");
		int toCurrencyChoice = sc.nextInt();
		toCode = currencyCodes.get(toCurrencyChoice);

		System.out.println("Amount you wish to convert?");
		amount = sc.nextDouble();

		sendHttpGetRequest(fromCode, toCode, amount);

		System.out.println("Thank you for using the Currency Converter!");

	}

	private static void sendHttpGetRequest(String fromCode, String toCode, double amount) throws IOException {

		String GET_URL = "http://api.exchangerate.host/convert?access_key=ff8b13597d7e9dd892c944f57cd59f66&from="
				+ fromCode + "&to=" + toCode + "&amount=" + amount;
		URL url = new URL(GET_URL);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");
		int responseCode = httpURLConnection.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) { // Success
			try (BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
				String inputLine;
				StringBuilder response = new StringBuilder();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				// Parse the response JSON
				JSONObject obj = new JSONObject(response.toString());
				if (obj.has("result")) {
					double convertedAmount = obj.getDouble("result");
					System.out.println(amount + fromCode + " = " + convertedAmount + toCode);
				} else {
					System.out.println("Unable to find converted amount in response.");
				}
			} catch (JSONException e) {
				System.out.println("JSON parsing error: " + e.getMessage());
			}
		} else {
			System.out.println("GET request failed!");
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println("Currency Converter Menu:");
			System.out.println("1. Convert Currency");
			System.out.println("2. Add Favorite Currency");
			System.out.println("3. View Favorite Currencies");
			System.out.println("4. Exit");
			System.out.print("Enter your choice: ");

			int choice = Integer.parseInt(reader.readLine());

			switch (choice) {
			case 1:
				convertCurrency();
				break;
			case 2:
				addFavoriteCurrency();

				break;
			case 3:
				viewFavoriteCurrencies();
				break;
			case 4:
				System.out.println("Goodbye!");
				System.exit(0);
				break;
			default:
				System.out.println("Invalid choice. Please enter a valid option.");
			}

		}
	}

}