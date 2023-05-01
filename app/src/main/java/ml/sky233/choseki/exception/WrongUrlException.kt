package ml.sky233.choseki.exception

class WrongUrlException(url: String) : Exception("The url is wrong!! cause by $url")