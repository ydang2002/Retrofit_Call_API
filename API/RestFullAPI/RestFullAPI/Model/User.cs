﻿namespace RestFullAPI.Model;

public class User
{
    public int Id { get; set; }

    public string? UserLoginId { get; set; }

    public string? UserPassword { get; set; }

    public string? UserName { get; set; }

    public string? UserRole { get; set; }
}