using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using RestFullAPI.Model;

namespace RestFullAPI.Data;

public static class ApplicationDbContextSeed
{
    public static void Initialize(IServiceProvider serviceProvider)
    {
        using var context = new ApplicationDbContext(serviceProvider.GetRequiredService<DbContextOptions<ApplicationDbContext>>());
        var environment = serviceProvider.GetRequiredService<IWebHostEnvironment>();
        SeedUsers(context, environment);
        SeedAccounts(context, environment);
    }

    private static void SeedUsers(ApplicationDbContext context, IWebHostEnvironment environment)
    {
        if (context.Users.Any())
        {
            return;
        }
        string relativePath = "Seeds/users.json";
        string filePath = Path.Combine(environment.WebRootPath, relativePath);
        var users = LoadJson<User>(filePath);
        context.Users.AddRange(users);
        context.SaveChanges();
    }

    private static void SeedAccounts(ApplicationDbContext context, IWebHostEnvironment environment)
    {
        if (context.Accounts.Any())
        {
            return;
        }

        string relativePath = "Seeds/accounts.json";
        string filePath = Path.Combine(environment.WebRootPath, relativePath);
        var accounts = LoadJson<Account>(filePath);
        context.Accounts.AddRange(accounts);
        context.SaveChanges();
    }

    private static List<T> LoadJson<T>(string filePath)
    {
        using StreamReader r = new StreamReader(filePath);
        string json = r.ReadToEnd();
        List<T> items = JsonConvert.DeserializeObject<List<T>>(json);
        return items;
    }
}