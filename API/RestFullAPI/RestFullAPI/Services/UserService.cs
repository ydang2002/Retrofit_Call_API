using Microsoft.EntityFrameworkCore;
using RestFullAPI.Data;
using RestFullAPI.Dto;

namespace RestFullAPI.Services;

public interface IUserService
{
    Task<List<UserDto>> GetUsers();
}

public class UserService : IUserService
{
    private readonly ApplicationDbContext _context;

    public UserService(ApplicationDbContext context)
    {
        _context = context ?? throw new ArgumentNullException(nameof(context));
    }

    public Task<List<UserDto>> GetUsers()
    {
        var users = _context.Users
                            .AsNoTracking()
                            .Select(a => new UserDto
                            {
                                Id = a.Id,
                                UserLoginId = a.UserLoginId,
                                UserName = a.UserName,
                                UserPassword = a.UserPassword,
                                UserRole = a.UserRole
                            })
                            .ToListAsync();
        return users;
    }
}