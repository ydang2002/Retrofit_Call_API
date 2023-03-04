using Microsoft.AspNetCore.Mvc;
using RestFullAPI.Dto;
using RestFullAPI.Services;
using System.Net;

namespace RestFullAPI.Controllers;

[Route("api/[controller]")]
[ApiController]
public class AcountsController : ControllerBase
{
    private readonly IAccountService _accountService;

    public AcountsController(IAccountService accountService)
    {
        _accountService = accountService ?? throw new ArgumentNullException(nameof(accountService));
    }

    [HttpGet]
    [ProducesResponseType(typeof(List<AccountDto>), (int)HttpStatusCode.OK)]
    public async Task<ActionResult<AccountDto>> GetAccounts()
    {
        var accounts = await _accountService.GetAccounts();
        return Ok(accounts);
    }
}