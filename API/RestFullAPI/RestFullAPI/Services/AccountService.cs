using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using RestFullAPI.Data;
using RestFullAPI.Dto;

namespace RestFullAPI.Services;

public interface IAccountService
{
    Task<List<AccountDto>> GetAccounts();
}

public class AccountService : IAccountService
{
    private readonly ApplicationDbContext _context;

    public AccountService(ApplicationDbContext context)
    {
        _context = context ?? throw new ArgumentNullException(nameof(context));
    }

    public Task<List<AccountDto>> GetAccounts()
    {
        var accounts = _context.Accounts
                            .AsNoTracking()
                            .Select(a => new AccountDto
                            {
                                Id = a.Id,
                                Username = a.Username,
                                Password = a.Password,
                            })
                            .ToListAsync();
        return accounts;
    }
}