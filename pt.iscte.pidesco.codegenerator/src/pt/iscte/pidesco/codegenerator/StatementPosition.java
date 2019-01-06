package pt.iscte.pidesco.codegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class StatementPosition {
	
	int startPosition = 0;
	int endPosition = 0;
	int totalLength = 0;
	
	public StatementPosition(int startPosition, int endPosition, int totalLength) {
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.totalLength = totalLength;
	}
	
	public static StatementPosition searchStatementInFile(File file, String searchingTerm) {
		int statmentStartPosition = 0, statmentEndPosition = 0, statmentTotalLength = 0;
		try {
			Scanner sc = new Scanner(file);
			int bracketCounter = 0;
			boolean bracketFound = false;
			boolean termFound = false;
			while(sc.hasNext()) {
				String line = sc.nextLine();
				if(line.contains(searchingTerm)){
					termFound = true;
				}
				statmentTotalLength += line.length();
				if(termFound) {
					if(statmentStartPosition == 0) {
						statmentStartPosition = statmentTotalLength;
					}else if(line.endsWith("}") && bracketCounter - 1 == 0) {
						statmentEndPosition = statmentTotalLength + line.length();
					}
	
					if(line.contains("{")) {
						bracketCounter++;
						bracketFound = true;
					}
					
					if(line.contains("}")) {
						bracketCounter--;
						bracketFound = true;
					}
					
					if(bracketCounter == 0 && bracketFound) {
						statmentTotalLength = statmentEndPosition - statmentStartPosition;
						break;
					}
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return new StatementPosition(statmentStartPosition, statmentEndPosition, statmentTotalLength);
	}
	
	public int getStartPosition() {
		return startPosition;
	}

	public void setStartMethodPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(int totalLength) {
		this.totalLength = totalLength;
	}
	
}
