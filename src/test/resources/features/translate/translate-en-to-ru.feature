@translate
Feature: Yandex Translator EN to RU

  Scenario: Authentication iam
    Given set base url '{config{auth.tokensUrl}}'
    And resource is '/iam/v1/tokens'
    And create response body from template 'oauth-v1.json'
      | yandexPassportOauthToken | {config{auth.yandexPassportOauthToken}} |
    When execute POST
    Then response code is 200
    And save response parameter 'iamToken' to 'token'

  Scenario Outline: Translator <wordEn> to russian
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
      |wordEn              |wordRu                     |
      |complete a test task|выполнить тестовое задание |
      |read a book         |читать книгу               |
      |a deer eating moss  |олень ест мох              |
