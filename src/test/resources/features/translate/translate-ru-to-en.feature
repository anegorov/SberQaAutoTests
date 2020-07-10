@translate
Feature: Yandex Translator RU to EN

  Scenario: Authentication iam
    Given set base url '{config{auth.tokensUrl}}'
    And resource is '/iam/v1/tokens'
    And create response body from template 'oauth-v1.json'
      | yandexPassportOauthToken | {config{auth.yandexPassportOauthToken}} |
    When execute POST
    Then response code is 200
    And reset payload
    And save response parameter 'iamToken' to 'token'

  Scenario Outline: Translator <wordRu> to english
    Given set base url '{config{base}}'
    And resource is '/translate/v2/translate'
    And headers are
      | Content-Type  | application/json |
      | Authorization | Bearer {{token}} |
    And create response body from template 'translate-request.json'
      |folder_id          | {config{folder_id}} |
      |texts              | <wordRu>            |
      |targetLanguageCode | en                  |
    When execute POST
    Then response code is 200
    And check response
      |translations[0].text | <wordEn> |
      |translations[0].detectedLanguageCode | ru |
  Examples:
    |wordRu|wordEn|
    |выполнить тестовое задание |complete a test task|
    |читать книгу               |read a book         |
    |олень ест мох              |a deer eating moss  |

#  Scenario: Translator sample to english
#    Given set base url '{config{base}}'
#    And resource is '/translate/v2/translate'
#    And headers are
#      | Content-Type  | application/json |
#      | Authorization | Bearer {{token}} |
#    And create response body from template 'translate-request.json'
#      |folder_id          | {config{folder_id}} |
#      |texts              | Мир                 |
#      |targetLanguageCode | en                  |
#    When execute POST
#    Then response code is 200
#    And check response
#      |translations[0].text | World |
#      |translations[0].detectedLanguageCode | ru |

