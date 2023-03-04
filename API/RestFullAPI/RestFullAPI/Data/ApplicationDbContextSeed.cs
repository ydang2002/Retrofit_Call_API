using Microsoft.EntityFrameworkCore;

namespace RestFullAPI.Data;

public static class ApplicationDbContextSeed
{
    public static void Initialize(IServiceProvider serviceProvider)
    {
        using var context = new ApplicationDbContext(serviceProvider.GetRequiredService<DbContextOptions<ApplicationDbContext>>());
        SeedUsers(context);
        SeedAccounts(context);
    }

    private static void SeedUsers(ApplicationDbContext context)
    {
        if (context.Users.Any())
        {
            return;
        }
    }

    private static void SeedAccounts(ApplicationDbContext context)
    {
        if (context.Accounts.Any())
        {
            return;
        }
    }
}