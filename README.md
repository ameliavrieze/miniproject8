Name: Amelia Vrieze
Purpose: An implementation of a simple list interface with circular doubly linked lists.
Acknowledgements: Files with an author credit to Sam Rebelsky are from the in-class lab on linked lists. Files with an author credit to both of us were modified from the original lab to add the circular element to the linked list implementation. 

How did using a dummy node simplify the code?
It cuts down on the number of special cases required for adding and removing nodes to the list. Adding to an empty list just adds after the dummy node. Adding to the end of a list adds before the dummy node. Removing all the elements of the list doesn't cause problems for adding elements again because the dummy node can't be removed. However, I did struggle for a while to make it so the dummy node was actually ignored and couldn't be removed or changed. It kept printing out along with the other nodes. 

Github: https://github.com/ameliavrieze/miniproject8
