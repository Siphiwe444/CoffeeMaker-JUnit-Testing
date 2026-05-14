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
 * 							 coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

/**
 * Unit tests for CoffeeMaker class.
 * 
 * @author Sarah Heckman
 */
public class CoffeeMakerTest {
	
	/**
	 * The object under test.
	 */
	private CoffeeMaker coffeeMaker;
	
	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;

	/**
	 * Initializes some recipes to test with and the {@link CoffeeMaker} 
	 * object we wish to test.
	 * 
	 * @throws RecipeException  if there was an error parsing the ingredient 
	 * 		amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
		coffeeMaker = new CoffeeMaker();
		
		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");
		
		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");
		
		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");
		
		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");
	}
	
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with well-formed quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.addInventory("4","7","0","9");
		
		String inventory = coffeeMaker.checkInventory();

		assertTrue(inventory.contains("Coffee"));
		assertTrue(inventory.contains("Milk"));
		assertTrue(inventory.contains("Sugar"));
		assertTrue(inventory.contains("Chocolate"));
	}
	
	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with malformed quantities (i.e., a negative 
	 * quantity and a non-numeric string)
	 * Then we get an inventory exception
	 * 
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}
	
	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying more than 
	 * 		the coffee costs
	 * Then we get the correct change back.
	 */
	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}

    @Test
    public void testAddDuplicateRecipe() {
	     assertTrue(coffeeMaker.addRecipe(recipe1));
	     assertFalse(coffeeMaker.addRecipe(recipe1));
    }

    @Test
    public void testAddMoreThanThreeRecipes() {
	    assertTrue(coffeeMaker.addRecipe(recipe1));
	    assertTrue(coffeeMaker.addRecipe(recipe2));
	    assertTrue(coffeeMaker.addRecipe(recipe3));
	    assertTrue(coffeeMaker.addRecipe(recipe4));
    }

    @Test
    public void testDeleteRecipe() {
 	     coffeeMaker.addRecipe(recipe1);

	     String deletedRecipe = coffeeMaker.deleteRecipe(0);

	    assertEquals("Coffee", deletedRecipe);
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

	    String editedRecipe = coffeeMaker.editRecipe(0, newRecipe);

	    assertEquals("Coffee", editedRecipe);

	    int change = coffeeMaker.makeCoffee(0, 100);

	    assertEquals(40, change);
   }   

   @Test
   public void testMakeCoffeeNotEnoughMoney() {
	   coffeeMaker.addRecipe(recipe1);

	   int change = coffeeMaker.makeCoffee(0, 25);

	   assertEquals(25, change);
    }

    @Test
    public void testInventoryDecreasesAfterCoffee() {
	   coffeeMaker.addRecipe(recipe1);

	    String inventoryBefore = coffeeMaker.checkInventory();

	    coffeeMaker.makeCoffee(0, 100);

	    String inventoryAfter = coffeeMaker.checkInventory();

	    assertNotEquals(inventoryBefore, inventoryAfter);
    }

    @Test(expected = RecipeException.class)
    public void testNegativeRecipePrice() throws RecipeException {
	    Recipe badRecipe = new Recipe();

	    badRecipe.setPrice("-10");
   }

   @Test(expected = RecipeException.class)
    public void testNegativeCoffeeAmount() throws RecipeException {
	  Recipe badRecipe = new Recipe();

	    badRecipe.setAmtCoffee("-1");
    }

   @Test
   public void testMakeCoffeeExactMoney() {
	   coffeeMaker.addRecipe(recipe1);

	   int change = coffeeMaker.makeCoffee(0, 50);

	   assertEquals(0, change);
   }

   @Test
   public void testDeleteRecipeActuallyRemovesRecipe() {
	   coffeeMaker.addRecipe(recipe1);

	   coffeeMaker.deleteRecipe(0);

	   int change = coffeeMaker.makeCoffee(0, 100);

	   assertEquals(100, change);
   }

   @Test
   public void testEditRecipeChangesPrice() throws RecipeException {
	   coffeeMaker.addRecipe(recipe1);

	   Recipe editedRecipe = new Recipe();
	   editedRecipe.setName("Coffee");
	   editedRecipe.setAmtChocolate("0");
	   editedRecipe.setAmtCoffee("3");
	   editedRecipe.setAmtMilk("1");
	   editedRecipe.setAmtSugar("1");
	   editedRecipe.setPrice("90");

	   coffeeMaker.editRecipe(0, editedRecipe);

	   int change = coffeeMaker.makeCoffee(0, 100);

	   assertEquals(10, change);
   }

   @Test
   public void testInventoryReducedCorrectly() {
	   coffeeMaker.addRecipe(recipe1);

	   String before = coffeeMaker.checkInventory();

	   coffeeMaker.makeCoffee(0, 100);

	   String after = coffeeMaker.checkInventory();

	   assertNotEquals(before, after);
   }

   @Test
   public void testMakeCoffeeInsufficientInventory() {
	   coffeeMaker.addRecipe(recipe2);

	   int change = coffeeMaker.makeCoffee(0, 100);

	   assertEquals(100, change);
   }

   @Test
   public void testAddRecipeReturnsTrue() {
	   assertTrue(coffeeMaker.addRecipe(recipe1));
   }

   @Test
   public void testDeleteRecipeReturnsName() {
	   coffeeMaker.addRecipe(recipe1);

	   assertEquals("Coffee", coffeeMaker.deleteRecipe(0));
   }

   @Test
   public void testRecipeSlotFreedAfterDelete() {
	   coffeeMaker.addRecipe(recipe1);
	   coffeeMaker.addRecipe(recipe2);
	   coffeeMaker.addRecipe(recipe3);

	   coffeeMaker.deleteRecipe(1);

	   assertTrue(coffeeMaker.addRecipe(recipe4));
   }

   @Test
   public void testCannotMakeCoffeeWithInvalidRecipeIndex() {
	   int change = coffeeMaker.makeCoffee(2, 100);

	   assertEquals(100, change);
   }

   @Test
   public void testInventoryNotChangedIfCoffeeFails() {
	   coffeeMaker.addRecipe(recipe1);

	   String before = coffeeMaker.checkInventory();

	   coffeeMaker.makeCoffee(0, 10);

	   String after = coffeeMaker.checkInventory();

	   assertEquals(before, after);
   }
}