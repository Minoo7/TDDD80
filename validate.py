from python_phone_validation import AbstractPhoneValidation

USERNAME_LENGTH_MIN = 5
NAME_LENGTH_MIN = 3
NAME_LENGTH_MAX = 32

PHONE_VAL_API_KEY = "d3e38e8aa0b4467686db3d073cfe450a"  # Get your API Key from https://app.abstractapi.com/api/phone-validation/documentation

AbstractPhoneValidation.configure(PHONE_VAL_API_KEY)


def validate_username(username):
	return USERNAME_LENGTH_MIN <= len(username) <= NAME_LENGTH_MAX


def validate_name(name):
	return NAME_LENGTH_MIN <= len(name) <= NAME_LENGTH_MAX and name.isalpha()


def validate_organization_number(number):
	return len(number) == 10


class InputNotValidError(ValueError):
	pass
