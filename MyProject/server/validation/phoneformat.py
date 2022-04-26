import re

subscriberNumberFormats = {
	5: '/^(\d{3})(\d{2})$/',
	6: '/^(\d{2})(\d{2})(\d{2})$/',
	7: '/^(\d{3})(\d{2})(\d{2})$/',
	8: '/^(\d{3})(\d{3})(\d{2})$/'
}

isSwedishMobilePhoneNumber = lambda val: bool(re.match('^07[02369]{1}\d{7}$', val))
isSwedishPhoneNumber = lambda val: val[0] == 0 and 6 < len(val) < 13
normalizeSwedishPhoneNumber = lambda val: re.sub('^\+46', '0', re.sub('[^\d\+]', '', val))
hasTwoLetterAreaCode = lambda val: bool(re.match('^08', val))
hasThreeLetterAreaCode = lambda val: bool(
	re.match('^0(11|13|16|18|19|21|23|26|31|33|35|36|40|42|44|46|54|60|63|90)', val))
formatSwedishMobilePhoneNumber = lambda val: re.sub('^(\d{3})(\d{3})(\d{2})(\d{2})$', r'\1-\2 \3 \4', val)
formatSubscriberNumber = lambda val: re.sub(subscriberNumberFormats[len(val)],
											(r'\1 \2' if len(val) == 5 else r'\1 \2 \3'), val)


def format_swedish_phone_number(val):
	area_code, subscriber_number = split_swedish_phone_number(val)
	return area_code + '-' + formatSubscriberNumber(subscriber_number)


def split_swedish_phone_number(val):
	pos = 2 if hasTwoLetterAreaCode(val) else (3 if hasThreeLetterAreaCode(val) else 4)
	return val[:pos], val[pos:]


def format_phone_number(phoneNumber):
	if phoneNumber:
		normalized = normalizeSwedishPhoneNumber(phoneNumber)

		if isSwedishMobilePhoneNumber(normalized):
			return formatSwedishMobilePhoneNumber(normalized)

		if isSwedishPhoneNumber(normalized):
			return format_swedish_phone_number(normalized)

		return phoneNumber