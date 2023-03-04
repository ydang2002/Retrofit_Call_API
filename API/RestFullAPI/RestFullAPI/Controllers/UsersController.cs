using Microsoft.AspNetCore.Mvc;
using RestFullAPI.Dto;
using RestFullAPI.Services;
using System.Net;

namespace RestFullAPI.Controllers;

[Route("api/[controller]")]
[ApiController]
public class UsersController : ControllerBase
{
    private readonly IUserService _userService;

    public UsersController(IUserService userService)
    {
        _userService = userService ?? throw new ArgumentNullException(nameof(userService));
    }

    [HttpGet]
    [ProducesResponseType(typeof(List<UserDto>), (int)HttpStatusCode.OK)]
    public async Task<ActionResult<List<UserDto>>> GetUsers()
    {
        var users = await _userService.GetUsers();
        return Ok(users);
    }
}