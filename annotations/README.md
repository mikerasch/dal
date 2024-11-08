# Purpose

The **Annotations module** replicates key marker annotations from the Spring Framework, enabling the classification of classes with specific roles, such as **Controller**, **Service**, **Client**, and more.

The true value of this module lies in its ability to define **ArchUnit rules** based on these annotations. Additionally, these annotations can model various components of your application's infrastructure, enhancing both structure and maintainability.

## Key Annotations:
- **@Controller**: Marks a class responsible for handling JavaFX UI events.
- **@Service**: Marks a class that contains the business logic of the application.
- **@Client**: Marks a class that facilitates communication with external services.

By using these annotations, you can enforce architectural constraints and ensure a consistent structure across the application.
