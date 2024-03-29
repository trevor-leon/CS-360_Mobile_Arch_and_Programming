# CS-360 Mobile Architecture and Programming

# The onHand Inventory application

This application features a login activity, inventory activity, details activity, and an SMS message updates activity. It uses a Login and an Inventory SQLite database to store the appropriate data.


The login activity allows the user to enter their username and password. After a value has been entered in each field, the Login and Create Account buttons are enabled on the screen. From here, a user can create an account and store the Login information in the SQLite database, or they can login and enter the inventory activity.


![Image](https://user-images.githubusercontent.com/72781990/239395898-5d3b4062-84ad-4706-a896-bc20f8793fb3.png)


Here in the inventory, the user can interact with the inventory items' quantities, which updates the database. They can also choose to set up SMS text alerts. The Add floating action button is not implemented.


![Image](https://user-images.githubusercontent.com/72781990/239395911-d9a81525-5384-4595-aa0c-6443f496c496.png)


If the user clicks the SMS icon, they will be prompted to allow SMS permissions


![Image](https://user-images.githubusercontent.com/72781990/239395934-0a030dd3-0f5f-41b2-9bcd-157d4be96ecc.png)


Finally, in the SMS updates activity, the user can input a phone number and low inventory amount to get SMS updates when an item's quantity falls below a specified point. Currently, it only sends Toasts to the screen rather than texts.


![Image](https://user-images.githubusercontent.com/72781990/239395961-30136291-e6bd-4781-9904-8962f8661e2c.png)


## Known Bugs

The application was initially intended to be able to open the details activity from the inventory activity by pressing the floating action button (FAB) or by clicking on an item in the inventory grid. Also, the SMS message updates activity does not properly send text messages at the moment, but it returns data as intended.

# Enhancement Plan

Category 1: Software Engineering/Design

a. the artifact name – The onHand Inventory application was created for CS-360: Mobile Architecture and Design in the Java and XML programming languages. It can be found here on GitHub: https://github.com/trevor-leon/CS-360_Mobile_Arch_and_Programming.

b. the enhancement plan – First, I want to convert or rewrite the login screen of the project in the Kotlin programming language using Jetpack Compose and following best practices so that it can be reused for any application. Best practices of development with Kotlin for Android include utilizing Coroutines for asynchronous tasks such as database access; Flows and unidirectional data flow (UDF) to get updated data from the user interface (UI) and updated UI; dependency injection for better code reusability; I will primarily be following the official Android courses here using Kotlin and Jetpack Compose to upgrade my project to the currently recommended toolkits. I lastly want to include a dark/light mode functionality using the guide found here: https://developer.android.com/develop/ui/views/theming/darktheme. 

c. the specific skills relevant to the course outcomes – In this project, I want to employ strategies for building collaborative environments by utilizing separation of concerns to allow for a more collaborative and scalable project and provide useful comments throughout the project. Additionally, I will showcase my newfound Kotlin skills in this project to show I can utilize innovative solutions as the course outcomes require. Also, a dark/light mode will allow more flexibility and a wider audience to enjoy my app according to their own preference. Below 
is the overall structure and plan of the Login activity:

<img width="488" alt="KotlinLoginOverview" src="https://github.com/trevor-leon/CS-360_Mobile_Arch_and_Programming/assets/72781990/543b9544-4a1e-4c5a-acc5-3158084c3589">


 
&emsp;When the app is started, the onCreate() method is called, which sets up the screen as shown above. A new user can input their username and password before tapping the “Create Account” button, which will save their credentials to the database securely. If the username is already in the database, it will not be inserted, and the user will be notified of an invalid entry. If the user taps the “Login” button, the input credentials will be verified against the credentials in the database, and the user will be directed accordingly. From here, the Login activity can be reused for any other application.

 
Category 2: Algorithms and Data Structures

a. the artifact name - The onHand Inventory application was created for CS-360: Mobile Architecture and Design in the Java and XML programming languages.

b. the enhancement plan – The Login and Create Account methods I already designed do not properly salt/encrypt data before use since I learned about that concept later in my studies. Before the user’s email and password are stored in the database, they should be properly encrypted using recommended encryption algorithms before storing the user data. To accomplish this, I will use a CryptoManager class and the SQLCipher class to implement encryption for the Room database. The basic data structure of a Login should also be considered. For example, every user account will contain an id, username, and password. Lastly, the emails and passwords should be checked for email and password patterns. For example, there is a Patterns class that contains an email address pattern that can be used to check the input username string.

c. the specific skills relevant to the course outcomes – I will demonstrate that I can design and evaluate computing solutions that solve a given problem using algorithmic principles and computer science practices and standards appropriate to its solution by properly utilizing encryption algorithms to better protect user data and checking input fields for proper input. Encrypting user data before storing it also demonstrates a security mindset that anticipates adversarial exploits in software architecture and designs to expose potential vulnerabilities, mitigate design flaws, and ensure privacy and enhanced security of data and resources by making it considerably harder for an attacker to use the encrypted data.

<img width="386" alt="DatabasePlan" src="https://github.com/trevor-leon/CS-360_Mobile_Arch_and_Programming/assets/72781990/e26f2823-936e-4217-9ded-c0c6d9a1bac8">


 
&emsp;The UI sends loginUiState data such as the input username and password to the ViewModel, which determines if it is valid before storing it or using it. The CryptoManager class provides an encryption key stored in an encrypted file on the device for the Room database configured with SQLCipher to use to encrypt the database. The input is then used by the database’s operations to read or write necessary data. When the user wants to log in or create an account, they will tap the respective button. This will send the current data in the text boxes to the LoginViewModel, which will use database operations to store, read, or manipulate it. Below is example of pseudocode of how the login/create account button functions would be implemented using my plan of pattern matching; as well as how user accounts should be verified before storing them in the database:

```
If User taps Login or Create Account button AND username textbox does NOT match email pattern:
	Turn the textbox RED and output invalid email Toast
If User taps Login or Create Account button AND password textbox text does NOT match secure password pattern (x amount of characters; using lowercase letters, uppercase letters, and at least one number and symbol)
	Turn the textbox RED and show output text explaining password rules
	
Else
	Login
```

```
If User taps Create Account button:
	Verify username/password combination pattern
	Salt/encrypt username
	Salt/encrypt password
	Verify username/password combination against Login database
	If username/password combination incorrect:
		Create a Toast stating invalid account creation
	Else:
		Login

If User taps Login button:
	Salt/encrypt username
	Salt/encrypt password
	Verify username/password combination against Login database
	If username/password combination incorrect:
		Create a Toast stating invalid login
	Else:
		Login
```

Category 3: Databases

a. the artifact name - The onHand Inventory application was created for CS-360: Mobile Architecture and Design in the Java and XML programming languages.

b. the enhancement plan – Firstly, I want to utilize a Room database, which is the recommended way to use SQL/SQLite databases with Jetpack Compose. I want to add specific IDs to each user in the database as primary keys, rather than only using the user’s unique username as done in the previous implementation. To accomplish this, I will follow the recommended Room guidelines defined here to implement the pipeline of components such as the database itself; data access objects (DAOs), and entities to represent individual Logins within the database.

c. the specific skills relevant to the course outcomes – I want to showcase my ability to use well-founded and innovative techniques, skills, and tools in computing practices for the purpose of implementing computer solutions that deliver value and accomplish industry-specific goals by using Room databases. As previously stated, Room databases are recommended to use as they serve as an abstraction layer to SQLite databases to prevent misuse and simplify the process. Doing so also demonstrates a security mindset that anticipates adversarial exploits in software architecture and designs to expose potential vulnerabilities, mitigate design flaws, and ensure privacy and enhanced security of data and resources as they are designed to verify queries, reduce boilerplate code that comes with using SQLite.

<img width="468" alt="DatabaseDaoEntityRelationship" src="https://github.com/trevor-leon/CS-360_Mobile_Arch_and_Programming/assets/72781990/340c667a-be6c-463d-bdb8-069c7b03dd2e">


 
ePortfolio Overall

&emsp;The skills I want to demonstrate for this ePortfolio are my software development skills including algorithms and data structure, my user experience design skills, and my secure coding skills. It’s crucial to design software with all kinds of users in mind in today’s world, and that’s what I hope to achieve. I believe I managed to cover each required course outcome in my Enhancement Plan above. I have a foundational knowledge of Kotlin that I am working on, and I am excited to learn more about it! In the end, I hope that my plans for enhancement are not too ambitious. The main thing I am concerned about is the complexity of the encryption algorithms. It’s certainly very abstract, and I hope it doesn’t lead to any errors down the line.
