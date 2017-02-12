vending-machine-kata
====================

This is a simple exercise vending-machine-kata - in which I simulate the... vending machine (https://en.wikipedia.org/wiki/Vending_machine)

Key aspects
-----------

* To show how I am using Test-Driven Development
* To show my skills in object and domain design (Object-Oriented Programming, Domain-Driven Design)
* To show craftsmanship of production and test code (Clean Code)
* To show usage of design patterns
* To show atomicity and readability of my commits

Vending machine specification
-----------------------------

1. Vending machine contains products.
2. Products can be of different types (i.e. Cola drink 0.25l, chocolate bar, mineral water 0.33l and so on).
3. Products are on shelves.
4. One shelve can contain only one type of product (but multiple products).
5. Each product type has its own price.
6. Machine has a display.
7. If we select shelve number, display should show product price.
8. You can buy products by putting money into machine. Machine accepts denominations 5, 2, 1, 0.5, 0.2, 0.1.
9. After inserting a coin, display shows amount that must be added to cover product price.
10. After selecting a shelve and inserting enough money we will get the product and the change (but machine has to have money to be able to return the change).
11. After selecting a shelve and inserting insufficient money to buy a product, user has to press "Cancel" to get their money back.
12. If machine does not have enough money to give the change it must show a warning message and return the money user has put, and it should not give the product.
13. Machine can return change using only money that was put into it (or by someone at start or by people who bought goods). Machine cannot create it's own money!

Post-implementation notes - what could be done better
-----------------------------------------------------

* There are several public methods (especially in VendingMachineModel) added solely to make testing easier (all of them are marked with 'TODO').
  Ugly! Should be refactored.
* Tests of VendingMachineModel and VendingMachineController are currently merged into one big test class: VendingMachineControllerModelAndControllerTest.
  It should be split. Tests of model should also be extended with cases validating its state.
* There is an implicit dependency between Key and Shelves: there should be just as many numeric keys as shelves.
  This dependency should be made explicit.
* There is another implicit dependency, between Coin and product price: each product price should be expressible in available coin denominations. Otherwise buying anything would be impossible.
  This dependency should be made explicit, too.
