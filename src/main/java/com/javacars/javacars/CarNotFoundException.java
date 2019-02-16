package com.javacars.javacars;

public class CarNotFoundException extends RuntimeException
{
    public CarNotFoundException(Long id)
    {
        super("Could not find Car");
    }
}
