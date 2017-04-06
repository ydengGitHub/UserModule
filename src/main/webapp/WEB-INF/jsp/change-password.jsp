 <%@include file="includes/header.jsp"%>

<div class="panel panel-default">

	<div class="panel-heading">
		<h3 class="panel-title">Change password</h3>
	</div>
	
	<div class="panel-body">
	
		<form:form modelAttribute="changePasswordForm" role="form">
		
			<form:errors />
			<div class="form-group">
				<form:label path="oldPassword">Type new password</form:label>
				<form:password path="oldPassword" class="form-control" placeholder="Old Password" />
				<form:errors cssClass="error" path="oldPassword" />
			</div>
			
			<div class="form-group">
				<form:label path="newPassword">Type new password</form:label>
				<form:password path="newPassword" class="form-control" placeholder="New Password" />
				<form:errors cssClass="error" path="newPassword" />
			</div>
			
			<div class="form-group">
				<form:label path="retypePassword">Retype new password</form:label>
				<form:password path="retypePassword" class="form-control" placeholder="Retype password" />
				<form:errors cssClass="error" path="retypePassword" />
			</div>
			
			<button type="submit" class="btn btn-default">Submit</button>
			
		</form:form>
	</div>
</div>