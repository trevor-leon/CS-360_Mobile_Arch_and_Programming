# CS-360_Mobile_Arch_and_Programming

# The onHand Inventory application

This application features a login activity, inventory, activity, details activity, and an SMS message updates activity. It uses a Login and an Inventory SQLite database to store the appropriate data.



![Image](https://user-images.githubusercontent.com/72781990/239395898-5d3b4062-84ad-4706-a896-bc20f8793fb3.png)



![Image](https://user-images.githubusercontent.com/72781990/239395911-d9a81525-5384-4595-aa0c-6443f496c496.png)



![Image](https://user-images.githubusercontent.com/72781990/239395934-0a030dd3-0f5f-41b2-9bcd-157d4be96ecc.png)



![Image](https://user-images.githubusercontent.com/72781990/239395961-30136291-e6bd-4781-9904-8962f8661e2c.png)


## Known Bugs

The application was initially intended to be able to open the details activity from the inventory activity by pressing the floating action button (FAB) or by clicking on an item in the inventory grid. Also, the SMS message updates activity does not properly send text messages at the moment, but it returns data as intended.

# Enhancement Plan

## Category 1: Software Engineering/Design

a. the artifact name – The onHand Inventory application was created for CS-340: Mobile Architecture and Design in the Java and XML programming languages. It can be found here on GitHub: https://github.com/trevor-leon/CS-360_Mobile_Arch_and_Programming.

b. the enhancement plan – First, I want to convert or rewrite the project in Kotlin. There is a guide on how to convert a Java project into a Kotlin project here: https://developer.android.com/kotlin/add-kotlin. I will primarily be following the official Android courses [here](https://developer.android.com/courses) using Kotlin and Jetpack Compose to upgrade my project to the currently recommended toolkits. I also want to expand on the functionality of the application. For example, I could add functionality to the inventory activity's Add Item floating action button (FAB). I created a Details activity when I originally made the project but couldn’t manage to properly link it from the grid or FAB. This was not required for the original project, and it is something that makes sense to have in an inventory application. The FAB, when clicked, should open the details activity to input the name, description, and quantity of the item. I also want to add functionality to the grid of items to open the details for the item that was clicked. I lastly want to include a dark/light mode functionality using the guide found here: https://developer.android.com/develop/ui/views/theming/darktheme. 

c. the specific skills relevant to the course outcomes – I want to showcase my newfound Kotlin skills in this project to show I can utilize innovative solutions as the course outcomes require. Also, a dark/light mode will allow more flexibility and a wider audience to enjoy the app. Additionally, including useful comments throughout the project will showcase that I can effectively communicate in a professional manner. I plan to add more functionality to the FAB (where it currently simply makes a toast) and the inventory grid items to open a Details activity. From there, the user can add; update; or delete the item, or they can tap back to return to the inventory. Below is the planned control flow of the enhancements:


## Category 2: Algorithms and Data Structures

a. the artifact name - The onHand Inventory application was created for CS-340: Mobile Architecture and Design in the Java and XML programming languages. It can be found here on GitHub: https://github.com/trevor-leon/CS-360_Mobile_Arch_and_Programming. 

b. the enhancement plan – I want to add functionality to sort and sort the inventory items alphabetically or by item quantity. Additionally, I would like to have a search feature within the top app bar of the app to efficiently search the database for an item. These changes would involve adding code to include more items in the drop-down in the menu bar of the app. Also, the buttons would need to link to another activity or fragment, which would return information to use to search or sort the inventory grid. After, the grid will have to be sorted or searched according to the user’s input.

c. the specific skills relevant to the course outcomes –  The search and sort enhancements will demonstrate that I can design, evaluate, and decide the proper solution to the problem at hand as specified by the course outcomes. Basically, I intend to add two new buttons, and another text box to the inventory activity of my app. The search button will take text input in a new textbox and securely and efficiently search the inventory database for items with the searched substring. The sort button will take the user to a new, simple screen allowing them to sort the inventory by name ascending/descending or quantity ascending/descending.

## Category 3: Databases

a. the artifact name - The onHand Inventory application was created for CS-340: Mobile Architecture and Design in the Java and XML programming languages. It can be found here on GitHub: https://github.com/trevor-leon/CS-360_Mobile_Arch_and_Programming.

b. the enhancement plan – I want to add specific user IDs to each user in the database, rather than using the user’s unique username so that the included SMS Updates activity can properly send updates. I want also to add phone numbers and account types (admin, associate, customer; for example) to the user accounts. Lastly, the Login and Create Account methods I already designed do not properly salt/encrypt data before use since I learned about that concept later in my studies. I also want to add functionality to check input email addresses to ensure they contain a ‘@’ character and check input passwords to ensure they are secure (for example, at least 12 characters; containing an uppercase; lowercase; number; and symbol) before creating/storing them in the database. Lastly, each item in the grid should have an ID in the database.

c. the specific skills relevant to the course outcomes – I want to showcase my ability to use well-founded, innovative, and secure coding techniques. Protecting data via encryption is key for any real-world scenario where a user might be trying to log in to their account. If it is not encrypted, it can be intercepted by a malicious user. My proposed enhancements just make sense when working with the data and functions I am. For example, an email address should include an ‘@’ symbol. Also, data needs to be encrypted BEFORE it is used or stored.
 
 
## ePortfolio Overall

The skills I want to demonstrate for this ePortfolio are my software development skills including algorithms and data structure, my user experience design skills, and my secure coding skills. It’s crucial to design software with all kinds of users in mind in today’s world, and that’s what I hope to achieve. I believe I managed to cover each required course outcome in my Enhancement Plan above. I have a foundational knowledge of Kotlin that I am working on, but I, unfortunately, don’t know everything about it. In the end, I hope that my plans for enhancement are not too ambitious. I think I can do it, especially with the right guidance.
