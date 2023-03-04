namespace RestFullAPI.RequestModels;

public class UploadAvatarReqModel
{
    public IFormFile File { get; set; }

    public string UserName { get; set; }
    
    public string Password { get; set; }
}