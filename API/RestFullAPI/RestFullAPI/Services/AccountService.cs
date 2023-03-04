using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using RestFullAPI.Data;
using RestFullAPI.Dto;
using RestFullAPI.Model;
using RestFullAPI.RequestModels;

namespace RestFullAPI.Services;

public interface IAccountService
{
    Task<List<AccountDto>> GetAccounts();

    Task<AccountDto> RegisterAsync(UploadAvatarReqModel reqModel);
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

    public async Task<AccountDto> RegisterAsync(UploadAvatarReqModel reqModel)
    {
        var account = new Account 
        {
            Username = reqModel.UserName,
            Password = reqModel.Password,
            Avt = "https://images.indepth.dev/images/2022/07/ava.jpg"
        };
        await _context.Accounts.AddAsync(account);
        await _context.SaveChangesAsync();
        return new AccountDto 
        { 
            Username = reqModel.UserName,
            Password = reqModel.Password,
            Avt = account.Avt
        };
    }
}