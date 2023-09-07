As there were no specific details included about the class that requires testing, a generalized format for writing unit tests in python using unittest framework will be demonstrated below:

```python
import unittest

class TestYourClass(unittest.TestCase):
    
    def setUp(self):
        self.instance = YourClass()  # Assuming YourClass is the class you want to test

    def test_method1(self):
        # Assuming method1 is a method in YourClass you want to test
        result = self.instance.method1()
        expected_result =  # The expected result from method1
        self.assertEqual(result, expected_result)

    def test_method2(self):
        result = self.instance.method2()  # Assuming method2 is another method in YourClass
        expected_result =  # The expected result from method2
        self.assertEqual(result, expected_result)
        
    # Add more methods as needed

if __name__ == '__main__':
    unittest.main()
```

Note: This is a basic example. Depending on the actual class and its methods, your test cases may need to include more complex structures or use different assertions (like self.assertListEqual for lists, self.assertDictEqual for dictionaries, etc.). Also, please replace the `YourClass`, `method1`, `method2` with the actual class and methods names you want to write unit tests for.