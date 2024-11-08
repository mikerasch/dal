## Purpose
Interface module goal is to provide a base interface for the application.
It is a contract that not only defines the needed concrete implementations, but also provides base concrete classes for Controller -> Service communication.

 For example, consider a scenario where we have the following classes
- HomeController
- HomeService
- TerminalExecutor
- AuthProvider

The layout looks like the following:
- **HomeController**
    - **HomeService**
        - **TerminalExecutor**
        - **AuthProvider**
      
In this case, the interface module will provide the following for you:
- HomeController, which is fully furnished for UI needs.
- HomeService interface which should be implemented by the adapter module.
- TerminalExecutor interface which should be implemented by the adapter module.
- AuthProvider interface which should be implemented by the adapter module. 


## Why Not Provide Complete Implementations for All Components?
Different companies or applications often have unique requirements and environments. Providing fully concrete implementations for all components would reduce flexibility and could lead to unnecessary constraints.

For example:

**API Documentation:** One company might use Swagger for API documentation, but another company may need the documentation URL to be accessed through a different port (e.g., base URL +1). While a central implementation could be provided, it's more efficient and flexible for users to implement their specific requirements as needed.

**Business Logic:** The HomeService may require different interactions with databases, external APIs, or internal systems depending on the company’s infrastructure. The Interface Module offers the blueprint, but it’s left to the user to define the exact behavior.