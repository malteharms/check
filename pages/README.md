# Pages

This module contains all pages which can be accessed via `NavController`. Those pages typically contain only the composable resources and some covertion function to get the `String` for an internal data type. This document describes, where to store different type of files for a page.

## Page Directly Structure
A page is a directory located in `de.malteharms.pages` package. A page **must** contain a `presentation` directory, where all composables are located. The root file to display the entire page **is typically** located in `[name]/presentation/[Name]Page.kt`.

The second directory beside `presentation` is the `domain` directory. This one can be created if the page needs any interface, which defines a page specific class. The interface or derived class ***should not** be used outside of the page package. If the class could be used outside of the `[page]` directory, consider to store the definition inside of the `utils` module.

The third directory is called `data` and should only contain page specific business logic. Most likely this logic can be outsourced and generalized into the `utils` module. But if not, the logic can be stored here. 

## Display data as text
Any text, which should be dislayed on the page, should be defined in an seperate file. This file **should** be called `UI.kt` and located the `presentation` directory. 

### Enum
```kotlin
// You can overload this function with other enum classes

fun display(value: EnumClass): String {
    return when (value) {
        // enum attribute -> String
        // Define an output for each attribute
    }
}
```

### Object specific
```kotlin
fun display(obj1: SomeObject) {
    // forward to an private function to display the object(s) specific text
}
```