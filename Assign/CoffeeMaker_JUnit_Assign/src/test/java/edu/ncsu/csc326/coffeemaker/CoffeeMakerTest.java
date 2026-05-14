/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 * 
 * Permission has been explicitly granted to the University of Minnesota 
 * Software Engineering Center to use and distribute this source for 
 * educational purposes, including delivering online education through
 * Coursera or other entities.  
 * 
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including 
 * fitness for purpose.
 * 
 * 
 * Modifications 
 * 20171114 - Ian De Silva - Updated to comply with JUnit 4 and to adhere to 
 * coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

public class CoffeeMakerTest {

	private CoffeeMaker coffeeMaker;

	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

	@Before
	public void setUp() throws RecipeException {

		coffeeMaker = new CoffeeMaker();

		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");

		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");

		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");

		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");
	}

	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.addInventory("4","7","0","9");

		String inv = coffeeMaker.checkInventory();

		assertNotNull(inv);
	}

	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}

	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);

		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}

	@Test
	public void testAddRecipeLimit() {

		assertTrue(coffeeMaker.addRecipe(recipe1));
		assertTrue(coffeeMaker.addRecipe(recipe2));
		assertTrue(coffeeMaker.addRecipe(recipe3));

		// 4th should fail (important mutation test target)
		assertFalse(coffeeMaker.addRecipe(recipe4));
	}

	@Test
	public void testMakeCoffeeInvalidIndex() {
		coffeeMaker.addRecipe(recipe1);

		// invalid index should NOT crash
		int change = coffeeMaker.makeCoffee(5, 100);

		assertEquals(100, change);
	}

	@Test
	public void testMakeCoffeeInsufficientMoney() {
		coffeeMaker.addRecipe(recipe1);

		int change = coffeeMaker.makeCoffee(0, 25);

		assertEquals(25, change);
	}

	@Test
	public void testDeleteRecipe() {

		coffeeMaker.addRecipe(recipe1);

		String deleted = coffeeMaker.deleteRecipe(0);

		assertEquals("Coffee", deleted);
	}

	@Test
	public void testDeleteEmptySlot() {

		// deleting empty should return null
		assertNull(coffeeMaker.deleteRecipe(0));
	}

	@Test
	public void testEditRecipe() throws RecipeException {

		coffeeMaker.addRecipe(recipe1);

		Recipe newRecipe = new Recipe();
		newRecipe.setName("Coffee");
		newRecipe.setAmtChocolate("2");
		newRecipe.setAmtCoffee("5");
		newRecipe.setAmtMilk("2");
		newRecipe.setAmtSugar("2");
		newRecipe.setPrice("60");

		String result = coffeeMaker.editRecipe(0, newRecipe);

		assertEquals("Coffee", result);
	}

	@Test
	public void testInventoryChangesAfterCoffee() {

		coffeeMaker.addRecipe(recipe1);

		String before = coffeeMaker.checkInventory();

		coffeeMaker.makeCoffee(0, 100);

		String after = coffeeMaker.checkInventory();

		assertNotEquals(before, after);
	}

	@Test(expected = RecipeException.class)
	public void testNegativePrice() throws RecipeException {
		Recipe r = new Recipe();
		r.setPrice("-10");
	}

	@Test(expected = RecipeException.class)
	public void testNegativeCoffee() throws RecipeException {
		Recipe r = new Recipe();
		r.setAmtCoffee("-1");
	}
}