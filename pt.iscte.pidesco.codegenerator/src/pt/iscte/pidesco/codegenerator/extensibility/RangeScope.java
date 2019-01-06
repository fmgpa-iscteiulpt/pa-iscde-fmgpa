package pt.iscte.pidesco.codegenerator.extensibility;

/**
 * ENUM with the purpose of giving the range where the statement of code that will be generated can be used.
 * INSIDECLASS - Can only be used inside the brackets of a class;
 * OUTOFCLASS - Can only be used outside the brackets of a class;
 * INSIDEMETHOD - Can only be used inside a method statement;
 * OUTSIDEMETHOD - Can only be used outside a method statement and inside the brackets of a class;
 * ALL - Can be used in all cases.
 **/
public enum RangeScope {
	INSIDECLASS, OUTOFCLASS, INSIDEMETHOD, OUTSIDEMETHOD, ALL
}