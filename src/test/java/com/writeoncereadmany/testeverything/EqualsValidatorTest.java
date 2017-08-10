package com.writeoncereadmany.testeverything;

import org.junit.Test;

public class EqualsValidatorTest {

    EqualsValidator validator = new EqualsValidator();

    @Test
    public void doesNotIdentifyProblemsWithEqualsWhenAllFieldsChecked() throws Exception {
        validator.assertEqualsMethodsSound("com.writeoncereadmany.testeverything.shapes");
    }

    @Test(expected = AssertionError.class)
    public void identifiesProblemsWithEqualsMethodWhenOneFieldIgnored() throws Exception {
        validator.assertEqualsMethodsSound("com.writeoncereadmany.testeverything.brokenshapes");
    }

}
