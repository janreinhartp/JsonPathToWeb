package com.reinhart.JsonToJsonPathWeb.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.opencsv.CSVWriter;

@Service
public class JsonService {

	private static final Logger log = LoggerFactory.getLogger(JsonService.class);

	public String readJsonFile() {
		try {

			File file = ResourceUtils.getFile("classpath:json/data.json");
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			StringBuilder builder = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}

			String jsonContent = builder.toString();

			return jsonContent;

		} catch (FileNotFoundException e) {
			log.error(e.toString());
		} catch (IOException e) {
			log.error(e.toString());
		}
		return null;

	}

	public List<String[]> CombineJsonPathandData(String json, List<String> jsonpath) {

		List<String[]> data = new ArrayList<>();
		data.add(new String[] { "JsonPath Dot Notation", "Value" });

		jsonpath.forEach((temp) -> {
			Object dataObject = JsonPath.parse(json).read(temp);
			String value = dataObject.toString();
			data.add(new String[] { temp, value });
		});

		return data;
	}

	public void saveToCsv(List<String[]> datatoprocess) {
		try (CSVWriter writer = new CSVWriter(new FileWriter("C:\\Dev.Acn\\CSV\\test.csv"))) {
			writer.writeAll(datatoprocess);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
	}

	public List<String> ShowJsonPath(String json, String params) {
		String jsonData = json;

		log.info(jsonData);
		Configuration conf = Configuration.builder().options(Option.AS_PATH_LIST).build();

		List<String> allPaths = JsonPath.using(conf).parse(json).read(params);

		List<String> dotNotationPath = new ArrayList<String>();
		allPaths.forEach((temp) -> {
			String jsonpath = temp;
			String DotNotation1 = jsonpath.replace("['", ".").replace("']", ".").replace("]", "].").replace("..", ".");
			;

			char tocheck = DotNotation1.charAt(DotNotation1.length() - 1);
			StringBuffer DotNotationFinal = new StringBuffer(DotNotation1);
			if (tocheck == '.') {

				DotNotationFinal.deleteCharAt(DotNotation1.length() - 1);
			}

			// Object dataObject = JsonPath.parse(jsonData).read(jsonpath);
			// String value = dataObject.toString();

			log.info("JsonPath : " + jsonpath + " " + "Dot Notation : " + DotNotationFinal.toString());

			dotNotationPath.add(DotNotationFinal.toString());
		});

		return dotNotationPath;

	}

	public String htmlBuilder(List<String> paths, String JsonData) {
		StringBuilder builder = new StringBuilder();

		// Append HTML HEAD AND BODY
		builder.append("<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>JsonPath and Values</title>\r\n"
				+ "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-iYQeCzEYFbKjA/T2uDLTpkwGzCiq6soy8tYaI1GyVh/UjpbCx/TYkiZhlZB6+fzT\" crossorigin=\"anonymous\">\r\n"
				+ "</head>\r\n" + "<body>\r\n"
				+ "    <div class=\"d-flex flex-column justify-content-center align-items-center\" >\r\n"
				+ "        <div class=\"w-50\">\r\n" + "            <h1 >JsonPath and Values</h1>\r\n"
				+ "        </div>\r\n" + "       \r\n" + "    <div class=\"w-50 \">\r\n"
				+ "    <table class=\"table table-striped\">\r\n" + "        <thead>\r\n" + "          <tr>\r\n"
				+ "            <th>JsonPath</th>\r\n" + "            <th>Value</th>\r\n" + "          \r\n"
				+ "          </tr>\r\n" + "        </thead>\r\n" + "        <tbody>");

		// Append HTML TABLE CONTENTS
		paths.forEach((temp) -> {
			String jsonpath = temp;
			Object dataObject = JsonPath.parse(JsonData).read(jsonpath);
			String value = dataObject.toString();

			log.info("JsonPath : " + jsonpath + "  " + "Value : " + value);

			builder.append("<tr>");
			builder.append("<th>" + jsonpath + "</th>");
			builder.append("<th>" + value + "</th>");
			builder.append("</tr>");
		});

		// Append HTML CLOSING TAGS
		builder.append("  </tbody>\r\n" + "      </table>\r\n" + "    </div>\r\n" + "</div>\r\n"
				+ "      <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.2.1/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-u1OknCvxWvY5kfmNBILK2hRnQC3Pr17a+RTT6rIHI7NnikvbZlHgTPOOmMi466C8\" crossorigin=\"anonymous\"></script>\r\n"
				+ "</body>\r\n" + "</html>");

		return builder.toString();
	}
}
